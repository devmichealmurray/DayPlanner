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

private const val TAG = "AddEventViewModel"

class AddEventViewModel(application: Application) : SplashActivityViewModel(application) {

    private val _returnEvent by lazy { MutableLiveData<EventEntity>() }
    val returnEvent: LiveData<EventEntity> get() = _returnEvent

    private val _errorMessage by lazy { MutableLiveData<String>() }
    val addEventErrorMessage: LiveData<String> get() = _errorMessage

    fun eventChecker(id: Long) {
        if (id > 0L) {
            getEventById(id)
        }
    }

    fun prepareEvent(
        uid: Long, dateId: String, title: String, date: Long, locationName: String,
        address: String, notes: String
    ) {
        if (uid > 0L) {
            val event = EventEntity(
                uid = uid,
                dateId = dateId,
                title = title,
                eventTime = date,
                locationName = locationName,
                address = address,
                notes = notes,
            )
            updateEvent(event)
        } else {
            val event = EventEntity(
                dateId = dateId,
                title = title,
                eventTime = date,
                locationName = locationName,
                address = address,
                notes = notes,
            )
            saveEventToDB(event)
        }
    }

    private fun saveEventToDB(event: EventEntity) {
        viewModelScope.launch {
            try {
                dbRepo.addEvent(event)
//                _toastMessage.value = "Event ${event.title} Saved!"
            } catch (e: Exception) {
                _errorMessage.value = e.message.toString()
            }
        }
    }

    private fun updateEvent(event: EventEntity) {
        viewModelScope.launch {
            try {
                dbRepo.updateEvent(event)
//                _toastMessage.value = "Event ${event.title} Updated"
            } catch (e: Exception) {
                _errorMessage.value = e.message.toString()
            }
        }
    }

    private fun getEventById(id: Long) {
        viewModelScope.launch {
            try {
                dbRepo.getEventById(id)
                    .flowOn(Dispatchers.IO)
                    .collect { _returnEvent.value = it }
            } catch (e: Exception) {
                _errorMessage.value = e.message.toString()
            }
        }
    }

}