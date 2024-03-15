package com.mcmouse88.paging_advanced

import android.content.Context
import androidx.lifecycle.ViewModelProvider
import androidx.savedstate.SavedStateRegistryOwner
import com.mcmouse88.paging_advanced.api.GitHubService
import com.mcmouse88.paging_advanced.data.GitHubRepository
import com.mcmouse88.paging_advanced.local.RepoDatabase
import com.mcmouse88.paging_advanced.ui.ViewModelFactory

/**
 * Class that handles object creation.
 * Like this, objects can be passed as parameters in the constructors and then replaced for
 * testing, where needed.
 */
object Injector {

    /**
     * Creates an instance of [GitHubRepository] based on the [GitHubService] and a
     * GithubLocalCache
     */
    private fun provideGithubRepository(context: Context): GitHubRepository {
        return GitHubRepository(GitHubService.create(), RepoDatabase.getInstance(context))
    }

    /**
     * Provides the [ViewModelProvider.Factory] that is then used to get a reference to
     * ViewModel objects.
     */
    fun provideViewModelFactory(owner: SavedStateRegistryOwner, context: Context): ViewModelProvider.Factory {
        return ViewModelFactory(owner, provideGithubRepository(context))
    }
}