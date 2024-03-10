package com.mcmouse88.paging_basic.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mcmouse88.paging_basic.data.Article
import com.mcmouse88.paging_basic.data.ArticleRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn

/**
 * ViewModel for the [ArticleActivity] screen.
 * The ViewModel works with the [ArticleRepository] to get the data.
 */
class ArticleViewModel(
    repository: ArticleRepository
) : ViewModel() {

    /**
     * Stream of [Article]s for the UI.
     */
    val items: StateFlow<List<Article>> = repository.articleStream
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(),
            initialValue = emptyList()
        )
}