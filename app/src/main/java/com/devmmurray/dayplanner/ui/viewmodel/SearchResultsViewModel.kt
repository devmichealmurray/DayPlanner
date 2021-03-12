package com.devmmurray.dayplanner.ui.viewmodel

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.devmmurray.dayplanner.data.model.entity.NewsEntity
import com.devmmurray.dayplanner.data.model.local.NewsArticle
import com.devmmurray.dayplanner.data.repository.ApiRepository
import com.devmmurray.dayplanner.util.JsonProcessing
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch


private const val TAG = "SearchResultsViewModel"

class SearchResultsViewModel(application: Application) : SplashActivityViewModel(application) {

    private val _searchErrorMessage by lazy { MutableLiveData<String>() }
    val searchErrorMessage: LiveData<String> get() = _searchErrorMessage

    private val _searchResultList by lazy { MutableLiveData<List<NewsArticle>>() }
    val searchResultList: LiveData<List<NewsArticle>> get() = _searchResultList

    fun getSearchResults(searchTerm: String) {
        viewModelScope.launch {
            try {
                val result = ApiRepository.searchGuardian(searchTerm)
                if (result.isSuccessful) {
                    val news = JsonProcessing.parseNewsArticles(result)
                    news.forEach { addNewsArticle(it) }
                }
            } catch (e: Exception) {
                _searchErrorMessage.value = "$TAG: getSearchResults -- ${e.message.toString()}"
            }
        }
    }

    private fun addNewsArticle(article: NewsEntity) {
        viewModelScope.launch {
            newsUseCases.addNewsArticle.invoke(article)
//            dbRepo.addNewsArticle(article)
        }
    }

    fun getNewsArticles() {
        viewModelScope.launch {
            try {
                newsUseCases.getNewsArticle.invoke()
//                dbRepo.getNewsArticles()
                    .flowOn(Dispatchers.IO)
                    .collect { entityList ->
                        val news = entityList.map { it.toNewsArticle() }
                        _searchResultList.value = news
                    }
            } catch (e: Exception) {
                _searchErrorMessage.value = "$TAG: getNewsArticles -- ${e.message.toString()}"
            }
        }
    }

}