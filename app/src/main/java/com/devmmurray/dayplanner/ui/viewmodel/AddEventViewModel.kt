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

    fun prepareEvent(
        dateId: String, title: String, date: Long, locationName: String,
        address: String, notes: String
    ) {
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

    private fun saveEventToDB(event: EventEntity) {
        viewModelScope.launch {
            try {
                dbRepo.addEvent(event)
            } catch (e: Exception) {
                /**
                 *
                 * Do Something Here
                 *
                 */
            }
        }
    }

    private fun updateEvent(event: EventEntity) {
        viewModelScope.launch {
            try {
                dbRepo.updateEvent(event)
            } catch (e: Exception) {
                /**
                 *
                 * Do Something Here
                 *
                 */
            }
        }
    }

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

}