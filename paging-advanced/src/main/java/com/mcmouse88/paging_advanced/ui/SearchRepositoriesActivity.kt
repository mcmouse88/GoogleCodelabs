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
import androidx.lifecycle.LiveData
import androidx.lifecycle.distinctUntilChanged
import androidx.lifecycle.map
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.OnScrollListener
import com.mcmouse88.paging_advanced.Injector
import com.mcmouse88.paging_advanced.databinding.ActivitySearchRepositoryBinding
import com.mcmouse88.paging_advanced.model.RepoSearchResult

// https://developer.android.com/codelabs/android-paging?hl=en#0

class SearchRepositoriesActivity : AppCompatActivity() {

    private var _binding: ActivitySearchRepositoryBinding? = null
    private val binding: ActivitySearchRepositoryBinding
        get() = _binding ?: error("ActivitySearchRepositoryBinding is null")

    private val viewModel by viewModels<SearchReposViewModel>(
        factoryProducer = { Injector.provideViewModelFactory(this) }
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
            uiActions = viewModel.accept
        )
    }

    /**
     * Binds the [UiState] provided by the [SearchReposViewModel] to the UI,
     * and allows the UI to feed back user actions to it.
     */
    private fun ActivitySearchRepositoryBinding.bindState(
        uiState: LiveData<UiState>,
        uiActions: (UiAction) -> Unit
    ) {
        val decoration = DividerItemDecoration(
            this@SearchRepositoriesActivity,
            DividerItemDecoration.VERTICAL
        )
        binding.list.addItemDecoration(decoration)
        list.adapter = adapter
        bundSearch(
            uiState = uiState,
            onQueryChanged = uiActions
        )

        bindList(
            uiState = uiState,
            onScrollChanged = uiActions
        )
    }

    private fun ActivitySearchRepositoryBinding.bundSearch(
        uiState: LiveData<UiState>,
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

        uiState
            .map(UiState::query)
            .distinctUntilChanged()
            .observe(this@SearchRepositoriesActivity, searchRepo::setText)
    }

    private fun ActivitySearchRepositoryBinding.updateRepoListFromInput(
        onQueryChanged: (UiAction.Search) -> Unit
    ) {
        searchRepo.text.trim().let {
            if (it.isNotEmpty()) {
                list.scrollToPosition(0)
                onQueryChanged.invoke(UiAction.Search(query = it.toString()))
            }
        }
    }

    private fun ActivitySearchRepositoryBinding.bindList(
        uiState: LiveData<UiState>,
        onScrollChanged: (UiAction.Scroll) -> Unit
    ) {
        setupScrollListener(onScrollChanged)

        uiState
            .map(UiState::searchResult)
            .distinctUntilChanged()
            .observe(this@SearchRepositoriesActivity) { result ->
                when (result) {
                    is RepoSearchResult.Success -> {
                        showEmptyList(result.data.isEmpty())
                        adapter.submitList(result.data)
                    }

                    is RepoSearchResult.Error -> {
                        Toast.makeText(
                            this@SearchRepositoriesActivity,
                            "\uD83D\uDE28 Oops ${result.error}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
    }

    private fun ActivitySearchRepositoryBinding.showEmptyList(show: Boolean) {
        emptyList.isVisible = show
        list.isVisible = !show
    }

    private fun ActivitySearchRepositoryBinding.setupScrollListener(
        onScrollChanged: (UiAction.Scroll) -> Unit
    ) {
        val layoutManager = list.layoutManager as LinearLayoutManager
        val scrollListener = object : OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val totalItemCount = layoutManager.itemCount
                val visibleItemCount = layoutManager.childCount
                val lastVisibleItem = layoutManager.findLastVisibleItemPosition()

                onScrollChanged(
                    UiAction.Scroll(
                        visibleItemCount = visibleItemCount,
                        lastVisibleItemPosition = lastVisibleItem,
                        totalItemCount = totalItemCount
                    )
                )
            }
        }
        list.addOnScrollListener(scrollListener)
    }
}