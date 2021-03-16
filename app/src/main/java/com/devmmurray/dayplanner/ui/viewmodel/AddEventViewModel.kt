package com.devmmurray.dayplanner.ui.viewmodel

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.devmmurray.dayplanner.data.model.entity.EventEntity
import com.devmmurray.dayplanner.data.model.local.Event
import com.devmmurray.dayplanner.util.time.TimeFlags
import com.devmmurray.dayplanner.util.time.TimeStampProcessing
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch

private const val TAG = "AddEventViewModel"

class AddEventViewModel(app: Application) : SplashActivityViewModel(app) {

    var savedDay = -1
    var savedMonth = -1
    var savedYear = -1
    var savedMillis: Long = 0

    private val _returnEvent by lazy { MutableLiveData<Event>() }
    val returnEvent: LiveData<Event> get() = _returnEvent

    private val _errorMessage by lazy { MutableLiveData<String>() }
    val addEventErrorMessage: LiveData<String> get() = _errorMessage

    private val _eventSaved by lazy { MutableLiveData<Boolean>() }
    val eventSaved: LiveData<Boolean> get() = _eventSaved

    private val _setDatePickerTime by lazy { MutableLiveData<String>() }
    val setDatePickerTime: LiveData<String> get() = _setDatePickerTime


    fun setNewTimeMillis(hour: Int, minute: Int) {
        val dateMillis = TimeStampProcessing
            .transformDateStringToMillis(
                "$savedDay-$savedMonth-$savedYear $hour:$minute"
            )
        savedMillis = dateMillis
        _setDatePickerTime.value =
            TimeStampProcessing.transformSystemTime(savedMillis, TimeFlags.FULL)
    }

    fun eventChecker(id: Long) {
        if (id > 0L) {
            getEventById(id)
        }
    }

    fun prepareEvent(
        uid: Long, title: String, locationName: String, address: String, notes: String
    ) {
        if (savedMillis > 0L) {
            if (uid > 0L) {
                val event = EventEntity(
                    uid = uid,
                    dateId = "$savedMonth-$savedDay-$savedYear",
                    title = title,
                    eventTime = savedMillis,
                    locationName = locationName,
                    address = address,
                    notes = notes,
                )
                saveEventToDB(event)
            } else {
                val event = EventEntity(
                    dateId = "$savedMonth-$savedDay-$savedYear",
                    title = title,
                    eventTime = savedMillis,
                    locationName = locationName,
                    address = address,
                    notes = notes,
                )
                saveEventToDB(event)
            }
        } else {
            _errorMessage.value = "No Date or Time Selected For This Event"
        }
    }

    private fun saveEventToDB(event: EventEntity) {
        viewModelScope.launch {
            try {
                eventsUseCases.addEvent.invoke(event)
                _eventSaved.value = true
            } catch (e: Exception) {
                _errorMessage.value = e.message.toString()
            }
        }
    }

    private fun getEventById(id: Long) {
        viewModelScope.launch {
            try {
                eventsUseCases.getEventById.invoke(id)
                    .flowOn(Dispatchers.IO)
                    .collect { event ->
                        _returnEvent.value = event.toEventObject()
                        event.eventTime?.let { savedMillis = it }
                    }
            } catch (e: Exception) {
                _errorMessage.value = e.message.toString()
            }
        }
    }

}