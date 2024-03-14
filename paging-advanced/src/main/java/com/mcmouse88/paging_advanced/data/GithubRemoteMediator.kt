package com.mcmouse88.paging_advanced.data

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import com.mcmouse88.paging_advanced.api.GitHubService
import com.mcmouse88.paging_advanced.local.RepoDatabase
import com.mcmouse88.paging_advanced.model.Repo

@OptIn(ExperimentalPagingApi::class)
class GithubRemoteMediator(
    private val query: String,
    private val service: GitHubService,
    private val database: RepoDatabase
) : RemoteMediator<Int, Repo>() {

    override suspend fun load(loadType: LoadType, state: PagingState<Int, Repo>): MediatorResult {
        TODO("Not yet implemented")
    }
}