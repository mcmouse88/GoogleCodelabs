package com.mcmouse88.paging_advanced.data

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.mcmouse88.paging_advanced.api.GitHubService
import com.mcmouse88.paging_advanced.local.RemoteKeys
import com.mcmouse88.paging_advanced.local.RepoDatabase
import com.mcmouse88.paging_advanced.model.Repo
import com.mcmouse88.paging_advanced.toEntityList

// GitHub page API is 1 based: https://developer.github.com/v3/#pagination
private const val GITHUB_STARTING_PAGE_INDEX = 1

@OptIn(ExperimentalPagingApi::class)
class GithubRemoteMediator(
    private val query: String,
    private val service: GitHubService,
    private val database: RepoDatabase
) : RemoteMediator<Int, Repo>() {

    override suspend fun load(loadType: LoadType, state: PagingState<Int, Repo>): MediatorResult {
        val page = 0 /* when (loadType) {
            LoadType.REFRESH -> TODO()
            LoadType.PREPEND -> TODO()
            LoadType.APPEND -> TODO()
        }*/

        return try {
            val apiResponse = service.searchRepos(query, page, state.config.pageSize)

            val repos = apiResponse.items
            val endOfPaginationReached = repos.isEmpty()
            database.withTransaction {
                // Clear all tables in the database
                if (loadType == LoadType.REFRESH) {
                    database.remoteKeysDao().clearRemoteKeys()
                    database.reposDao().clearRepos()
                }

                val prevKey = if (page == GITHUB_STARTING_PAGE_INDEX) null else page - 1
                val nextKey = if (endOfPaginationReached) null else page + 1
                val keys = repos.map {
                    RemoteKeys(repoId = it.id, prevKey = prevKey, nextKey = nextKey)
                }
                database.remoteKeysDao().insertAll(keys)
                database.reposDao().insertAll(repos.toEntityList())
            }
            MediatorResult.Success(endOfPaginationReached = endOfPaginationReached)
        } catch (e: Exception) {
            MediatorResult.Error(e)
        }
    }
}