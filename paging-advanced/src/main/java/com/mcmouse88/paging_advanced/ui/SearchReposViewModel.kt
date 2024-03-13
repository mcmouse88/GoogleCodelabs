package com.mcmouse88.paging_advanced.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.distinctUntilChanged
import androidx.lifecycle.liveData
import androidx.lifecycle.switchMap
import androidx.lifecycle.viewModelScope
import com.mcmouse88.paging_advanced.data.GitHubRepository
import com.mcmouse88.paging_advanced.model.RepoSearchResult
import kotlinx.coroutines.launch

/**
 * ViewModel for the [SearchRepositoriesActivity] screen.
 * The ViewModel works with the [GitHubRepository] to get the data.
 */
class SearchReposViewModel(
    private val repository: GitHubRepository,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    /**
     * Stream of immutable states representative of the UI.
     */
    val state: LiveData<UiState>

    /**
     * Processor of side effects from the UI which in turn feedback into [state]
     */
    val accept: (UiAction) -> Unit

    init {
        val queryLiveData = MutableLiveData(savedStateHandle[LAST_SEARCH_QUERY] ?: DEFAULT_QUERY)

        state = queryLiveData
            .distinctUntilChanged()
            .switchMap { queryString ->
                liveData {
                    /*val uiState = repository.getSearchResultStream(queryString)
                        .map {
                            UiState(
                                query = queryString,
                                searchResult = it
                            )
                        }.asLiveData(Dispatchers.Main)
                    emitSource(uiState)*/
                }
            }

        accept = { action ->
            when (action) {
                is UiAction.Scroll -> {
                    if (action.shouldFetchMore) {
                        val immutableQuery = queryLiveData.value
                        if (immutableQuery != null) {
                            viewModelScope.launch {
                               // repository.requestMore(immutableQuery)
                            }
                        }
                    }
                }
                is UiAction.Search -> {
                    queryLiveData.postValue(action.query)
                }
            }
        }
    }

    override fun onCleared() {
        savedStateHandle[LAST_SEARCH_QUERY] = state.value?.query
        super.onCleared()
    }
}

sealed interface UiAction {
    data class Search(val query: String): UiAction
    data class Scroll(
        val visibleItemCount: Int,
        val lastVisibleItemPosition: Int,
        val totalItemCount: Int
    ) : UiAction
}

private val UiAction.Scroll.shouldFetchMore
    get() = visibleItemCount + lastVisibleItemPosition + VISIBLE_THRESHOLD >= totalItemCount

data class UiState(
    val query: String,
    val searchResult: RepoSearchResult
)

private const val VISIBLE_THRESHOLD = 5
private const val LAST_SEARCH_QUERY: String = "last_search_query"
private const val DEFAULT_QUERY = "Android"