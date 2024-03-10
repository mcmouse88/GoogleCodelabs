package com.mcmouse88.paging_basic.data

/**
 * Repository class that mimics fetching [Article] instances from an asynchronous source.
 */
class ArticleRepository {
    fun articlePagingSource(): ArticlePagingSource = ArticlePagingSource()
}