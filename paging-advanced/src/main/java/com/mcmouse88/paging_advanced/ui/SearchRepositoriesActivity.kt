package com.mcmouse88.paging_advanced.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.KeyEvent
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.paging.CombinedLoadStates
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import com.mcmouse88.paging_advanced.Injector
import com.mcmouse88.paging_advanced.databinding.ActivitySearchRepositoryBinding
import com.mcmouse88.paging_advanced.model.UiModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.scan
import kotlinx.coroutines.launch

// https://developer.android.com/codelabs/android-paging?hl=en#0

class SearchRepositoriesActivity : AppCompatActivity() {

    private var _binding: ActivitySearchRepositoryBinding? = null
    private val binding: ActivitySearchRepositoryBinding
        get() = _binding ?: error("ActivitySearchRepositoryBinding is null")

    private val viewModel by viewModels<SearchReposViewModel>(
        factoryProducer = { Injector.provideViewModelFactory(owner = this, context = this) }
    )

    private val adapter: ReposAdapter by lazy(LazyThreadSafetyMode.NONE) {
        ReposAdapter(
            onItemClick = { url ->
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                startActivity(intent)
            }
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivitySearchRepositoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.bindState(
            uiState = viewModel.state,
            pagingData = viewModel.pagingDataFlow,
            uiActions = viewModel.accept
        )
    }

    /**
     * Binds the [UiState] provided by the [SearchReposViewModel] to the UI,
     * and allows the UI to feed back user actions to it.
     */
    private fun ActivitySearchRepositoryBinding.bindState(
        uiState: StateFlow<UiState>,
        pagingData: Flow<PagingData<UiModel>>,
        uiActions: (UiAction) -> Unit
    ) {
        val decoration = DividerItemDecoration(
            this@SearchRepositoriesActivity,
            DividerItemDecoration.VERTICAL
        )

        binding.list.addItemDecoration(decoration)
        val header = ReposLoadStateAdapter { adapter.retry() }
        list.adapter = adapter.withLoadStateHeaderAndFooter(
            header = header,
            footer = ReposLoadStateAdapter { adapter.retry() }
        )

        bindSearch(
            uiState = uiState,
            onQueryChanged = uiActions
        )

        bindList(
            header = header,
            uiState = uiState,
            pagingData = pagingData,
            onScrollChanged = uiActions
        )
    }

    private fun ActivitySearchRepositoryBinding.bindSearch(
        uiState: StateFlow<UiState>,
        onQueryChanged: (UiAction.Search) -> Unit
    ) {
        searchRepo.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_GO) {
                updateRepoListFromInput(onQueryChanged)
                true
            } else {
                false
            }
        }

        searchRepo.setOnKeyListener { _, keyCode, event ->
            if (event.action == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
                updateRepoListFromInput(onQueryChanged)
                true
            } else {
                false
            }
        }

        lifecycleScope.launch {
            uiState
                .map { it.query }
                .distinctUntilChanged()
                .collect(searchRepo::setText)
        }
    }

    private fun ActivitySearchRepositoryBinding.updateRepoListFromInput(
        onQueryChanged: (UiAction.Search) -> Unit
    ) {
        searchRepo.text?.trim()?.let {
            if (it.isNotEmpty()) {
                onQueryChanged.invoke(UiAction.Search(query = it.toString()))
            }
        }
    }

    private fun ActivitySearchRepositoryBinding.bindList(
        header: ReposLoadStateAdapter,
        uiState: StateFlow<UiState>,
        pagingData: Flow<PagingData<UiModel>>,
        onScrollChanged: (UiAction.Scroll) -> Unit
    ) {
        list.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                if (dy != 0) onScrollChanged.invoke(UiAction.Scroll(uiState.value.query))
            }
        })

        btnRetry.setOnClickListener { adapter.retry() }

        val notLoading = adapter.loadStateFlow
            .asRemotePresentationState()
            .map { it == RemotePresentationState.PRESENTED }

        val hasNotScrolledForCurrentSearch = uiState
            .map { it.hasNotScrolledForCurrentSearch }
            .distinctUntilChanged()

        val shouldScrollToTop = combine(
            notLoading,
            hasNotScrolledForCurrentSearch,
            Boolean::and
        ).distinctUntilChanged()

        lifecycleScope.launch {
            adapter.loadStateFlow.collect { loadState ->
                // Show a retry header if there was an error refreshing, and items were previously
                // cached OR default to the default prepend state
                header.loadState = loadState.mediator
                    ?.refresh
                    ?.takeIf { it is LoadState.Error && adapter.itemCount > 0 }
                    ?: loadState.prepend

                val isListEmpty = loadState.refresh is LoadState.NotLoading
                        && adapter.itemCount == 0
                // Show empty list
                emptyList.isVisible = isListEmpty
                // Only show the list if refresh succeeds, either from the local db or the remote
                list.isVisible = loadState.source.refresh is LoadState.NotLoading
                        || loadState.mediator?.refresh is LoadState.NotLoading
                // Show loading spinner during initial load or refresh
                progressBar.isVisible = loadState.mediator?.refresh is LoadState.Loading
                // Show the retry state if initial load or refresh fails.
                btnRetry.isVisible =
                    loadState.mediator?.refresh is LoadState.Error && adapter.itemCount == 0

                // Toast on any error, regardless of whether it came from RemoteMediator or PagingSource
                val errorState = loadState.source.append as? LoadState.Error
                    ?: loadState.source.prepend as? LoadState.Error
                    ?: loadState.append as? LoadState.Error
                    ?: loadState.prepend as? LoadState.Error

                errorState?.let {
                    Toast.makeText(
                        this@SearchRepositoriesActivity,
                        "\\uD83D\\uDE28 Oops ${it.error}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }

        lifecycleScope.launch {
            pagingData.collectLatest(adapter::submitData)
        }

        lifecycleScope.launch {
            shouldScrollToTop.collect {
                if (it) list.scrollToPosition(0)
            }
        }
    }
}

enum class RemotePresentationState {
    INITIAL, REMOTE_LOADING, SOURCE_LOADING, PRESENTED
}

fun Flow<CombinedLoadStates>.asRemotePresentationState(): Flow<RemotePresentationState> {
    return scan(RemotePresentationState.INITIAL) { state, loadState ->
        when (state) {
            RemotePresentationState.INITIAL -> {
                when (loadState.mediator?.refresh) {
                    is LoadState.Loading -> RemotePresentationState.REMOTE_LOADING
                    else -> state
                }
            }

            RemotePresentationState.REMOTE_LOADING -> {
                when (loadState.source.refresh) {
                    is LoadState.Loading -> RemotePresentationState.SOURCE_LOADING
                    else -> state
                }
            }

            RemotePresentationState.SOURCE_LOADING -> {
                when (loadState.source.refresh) {
                    is LoadState.NotLoading -> RemotePresentationState.PRESENTED
                    else -> state
                }
            }

            RemotePresentationState.PRESENTED -> {
                when (loadState.mediator?.refresh) {
                    is LoadState.Loading -> RemotePresentationState.REMOTE_LOADING
                    else -> state
                }
            }
        }
    }.distinctUntilChanged()
}