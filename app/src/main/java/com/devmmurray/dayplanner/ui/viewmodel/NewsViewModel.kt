package com.devmmurray.dayplanner.ui.viewmodel

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.devmmurray.dayplanner.data.model.entity.NewsEntity
import com.devmmurray.dayplanner.data.model.local.SuggestionObject
import com.devmmurray.dayplanner.data.model.local.Suggestions
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch

class NewsViewModel(application: Application) : SplashActivityViewModel(application) {

    private val _newsList by lazy { MutableLiveData<List<NewsEntity>>() }
    val newsList: LiveData<List<NewsEntity>> get() = _newsList

    private val _newsErrorMessage by lazy { MutableLiveData<String>() }
    val newsErrorMessage: LiveData<String> get() = _newsErrorMessage

    fun getCurrentNews() {
        viewModelScope.launch {
            try {
                dbRepo.getNewsArticles()
                    .flowOn(Dispatchers.IO)
                    .collect {
                        _newsList.value = it
                    }
            } catch (e: Exception) {
                _newsErrorMessage.value = e.message.toString()
            }
        }
    }


    fun getSuggestionObjects(): List<SuggestionObject> {
        val objectsList = ArrayList<SuggestionObject>()
        val suggestionList = Suggestions.values().map { it.toString() }
        suggestionList.forEach {
            val suggestion = SuggestionObject(it)
            objectsList.add(suggestion)
        }
        return objectsList
    }

}