package com.mcmouse88.paging_advanced.data

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.mcmouse88.paging_advanced.api.GitHubService
import com.mcmouse88.paging_advanced.local.RepoDatabase
import com.mcmouse88.paging_advanced.local.RepoEntity
import kotlinx.coroutines.flow.Flow

/**
 * Repository class that works with local and remote data sources.
 */
class GitHubRepository(
    private val service: GitHubService,
    private val database: RepoDatabase
) {
    @OptIn(ExperimentalPagingApi::class)
    fun getSearchResultStream(query: String): Flow<PagingData<RepoEntity>> {
        // appending '%' so we can allow other characters to be before and after the query string
        val dbQuery = "%${query.replace(' ', '%')}%"
        return Pager(
            config = PagingConfig(
                pageSize = NETWORK_PAGE_SIZE,
                enablePlaceholders = false
            ),
            remoteMediator = GithubRemoteMediator(
                query = query,
                service = service,
                database = database
            ),
            pagingSourceFactory = { database.reposDao().reposByName(dbQuery) }
        ).flow
    }

    companion object {
        const val NETWORK_PAGE_SIZE = 30
    }
}