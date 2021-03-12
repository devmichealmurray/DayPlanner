package com.devmmurray.dayplanner.data.repository.datasource

import com.devmmurray.dayplanner.data.model.entity.NewsEntity
import kotlinx.coroutines.flow.Flow

interface NewsDataSource {
    suspend fun addNewsArticle(article: NewsEntity)
    fun getNewsArticles(): Flow<List<NewsEntity>>
    suspend fun deleteOldNews()
}