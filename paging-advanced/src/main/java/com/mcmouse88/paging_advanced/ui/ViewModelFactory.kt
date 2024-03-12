package com.mcmouse88.paging_advanced.ui

import androidx.lifecycle.AbstractSavedStateViewModelFactory
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.savedstate.SavedStateRegistryOwner
import com.mcmouse88.paging_advanced.data.GitHubRepository

/**
 * Factory for ViewModels
 */
class ViewModelFactory(
    owner: SavedStateRegistryOwner,
    private val repository: GitHubRepository
) : AbstractSavedStateViewModelFactory(owner, null) {

    override fun <T : ViewModel> create(
        key: String,
        modelClass: Class<T>,
        handle: SavedStateHandle
    ): T {
        if (modelClass.isAssignableFrom(SearchReposViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return SearchReposViewModel(repository, handle) as T
        }
        error("Unknown ViewModel class: $modelClass")
    }
}