package com.devmmurray.dayplanner.data.model.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.devmmurray.dayplanner.data.model.local.NewsArticle


@Entity(tableName = "news_articles")
class NewsEntity(
    @PrimaryKey(autoGenerate = true)
    var uid: Long = 0L,
    @ColumnInfo(name = "section_name")
    val sectionName: String?,
    @ColumnInfo(name = "publish_date")
    val pubDate: String?,
    @ColumnInfo(name = "news_title")
    val title: String?,
    @ColumnInfo(name = "url")
    val url: String?,
    @ColumnInfo(name = "pillar_name")
    val pillarName: String?
) {
    fun toNewsArticle() = NewsArticle(
        uid, sectionName, pubDate, title, url, pillarName
    )
}