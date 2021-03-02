package com.devmmurray.dayplanner.ui.viewmodel

import android.app.Application
import androidx.lifecycle.viewModelScope
import com.devmmurray.dayplanner.data.model.entity.EventEntity
import kotlinx.coroutines.launch

private const val TAG = "AddEventViewModel"

class AddEventViewModel(application: Application) : SplashActivityViewModel(application) {

    fun prepareEvent(
        dateId: String,
        title: String,
        date: Long,
        locationName: String,
        address: String,
        notes: String
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
}