package com.devmmurray.dayplanner.data.usecases.news

import com.devmmurray.dayplanner.data.model.entity.NewsEntity
import com.devmmurray.dayplanner.data.repository.dbrepos.NewsRepository

class AddNewsArticle(
    private val newsRepository: NewsRepository
) {
    suspend operator fun invoke(article: NewsEntity) =
        newsRepository.addNewsArticle(article)
}

class GetNewsArticle(
    private val newsRepository: NewsRepository
) {
    operator fun invoke() =
        newsRepository.getNewsArticles()
}

class DeleteOldNews(
    private val newsRepository: NewsRepository
) {
    suspend operator fun invoke() =
        newsRepository.deleteOldNews()
}