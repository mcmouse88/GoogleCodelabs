package com.mcmouse88.paging_advanced.local

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert

@Dao
interface ReposDao {

    @Upsert
    suspend fun insertAll(repos: List<RepoEntity>)

    @Query("""
        SELECT * FROM repos
        WHERE name LIKE :queryString
        ORDER BY stargazers_count DESC, name ASC
    """)
    fun reposByName(queryString: String): PagingSource<Int, RepoEntity>

    @Query("DELETE FROM repos")
    suspend fun clearRepos()
}