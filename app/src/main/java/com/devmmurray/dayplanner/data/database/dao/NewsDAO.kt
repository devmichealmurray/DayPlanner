package com.devmmurray.dayplanner.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.devmmurray.dayplanner.data.model.entity.NewsEntity
import kotlinx.coroutines.flow.Flow


@Dao
interface NewsDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addNewsArticle(article: NewsEntity)

    @Query("SELECT * FROM news_articles")
    fun getNewsArticles(): Flow<List<NewsEntity>>

    @Query("DELETE FROM news_articles")
    suspend fun deleteOldNewsArticles()
}