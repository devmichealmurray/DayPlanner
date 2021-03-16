package com.devmmurray.dayplanner.ui.viewmodel

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.devmmurray.dayplanner.data.model.local.NewsArticle
import com.devmmurray.dayplanner.data.model.local.SuggestionObject
import com.devmmurray.dayplanner.data.model.local.Suggestions
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch

class NewsViewModel(app: Application) : SplashActivityViewModel(app) {

    private val _newsList by lazy { MutableLiveData<List<NewsArticle>>() }
    val newsList: LiveData<List<NewsArticle>> get() = _newsList

    private val _newsErrorMessage by lazy { MutableLiveData<String>() }
    val newsErrorMessage: LiveData<String> get() = _newsErrorMessage

    private val _suggestionsList by lazy { MutableLiveData<List<SuggestionObject>>() }
    val suggestionList: LiveData<List<SuggestionObject>> get() = _suggestionsList

    fun getCurrentNews() {
        viewModelScope.launch {
            try {
                newsUseCases.getNewsArticle.invoke()
                    .flowOn(Dispatchers.IO)
                    .collect { list ->
                        val articleList = list.map { it.toNewsArticle() }
                        _newsList.value = articleList
                    }
            } catch (e: Exception) {
                _newsErrorMessage.value = e.message.toString()
            }
        }
    }

    fun getSuggestions() {
        val suggestionList = ArrayList<SuggestionObject>()
        val enumStringValues = Suggestions.values().map { it.toString() }
        enumStringValues.forEach { string ->
            SuggestionObject(string).also { suggestionList.add(it) }
        }
        _suggestionsList.value = suggestionList.toList()
    }

}