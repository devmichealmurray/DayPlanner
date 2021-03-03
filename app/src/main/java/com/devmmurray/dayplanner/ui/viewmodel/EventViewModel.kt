package com.devmmurray.dayplanner.ui.viewmodel

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.devmmurray.dayplanner.data.model.entity.EventEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch

class EventViewModel(application: Application): SplashActivityViewModel(application) {

    private val _returnEvent by lazy { MutableLiveData<EventEntity>() }
    val returnEvent: LiveData<EventEntity> get() = _returnEvent

    fun getEventById(id: Long) {
        viewModelScope.launch {
            try {
                dbRepo.getEventById(id)
                    .flowOn(Dispatchers.IO)
                    .collect { _returnEvent.value = it }

            } catch (e: Exception) {

                /**
                 *
                 * Do Something with the error
                 *
                 */

            }
        }
    }

    fun deleteEvent(id: Long) {
        viewModelScope.launch {
            try {
                dbRepo.deleteEvent(id)
            } catch (e: Exception) {
                /**
                 *
                 * Do Something with the error
                 *
                 */
            }
        }
    }
}