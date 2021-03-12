package com.devmmurray.dayplanner.data.database.roomdatasource

import android.content.Context
import com.devmmurray.dayplanner.data.database.RoomDatabaseClient
import com.devmmurray.dayplanner.data.model.entity.NewsEntity
import com.devmmurray.dayplanner.data.repository.datasource.NewsDataSource
import kotlinx.coroutines.flow.Flow

class RoomNewDataSource(context: Context) : NewsDataSource {
    private val newsDao = RoomDatabaseClient.getDbInstance(context).newsDAO()

    override suspend fun addNewsArticle(article: NewsEntity) =
        newsDao.addNewsArticle(article)

    override fun getNewsArticles() : Flow<List<NewsEntity>> = newsDao.getNewsArticles()

    override suspend fun deleteOldNews() = newsDao.deleteOldNewsArticles()
}