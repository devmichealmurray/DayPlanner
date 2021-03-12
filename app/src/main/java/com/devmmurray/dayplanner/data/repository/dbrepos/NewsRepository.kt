package com.devmmurray.dayplanner.data.repository.dbrepos

import com.devmmurray.dayplanner.data.model.entity.NewsEntity
import com.devmmurray.dayplanner.data.repository.datasource.NewsDataSource

class NewsRepository(
    private val newsDataSource: NewsDataSource
) {
    suspend fun addNewsArticle(article: NewsEntity) =
        newsDataSource.addNewsArticle(article)

    fun getNewsArticles() =
        newsDataSource.getNewsArticles()

    suspend fun deleteOldNews() =
        newsDataSource.deleteOldNews()
}