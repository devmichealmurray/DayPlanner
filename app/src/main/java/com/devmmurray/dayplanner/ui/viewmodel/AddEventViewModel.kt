package com.devmmurray.dayplanner.ui.viewmodel

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.devmmurray.dayplanner.data.model.entity.EventEntity
import com.devmmurray.dayplanner.data.model.local.Event
import com.devmmurray.dayplanner.util.flags.DatePickerFlags
import com.devmmurray.dayplanner.util.time.TimeFlags
import com.devmmurray.dayplanner.util.time.TimeStampProcessing
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch

private const val TAG = "AddEventViewModel"

class AddEventViewModel(app: Application) : SplashActivityViewModel(app) {

    private var savedDay = -1
    private var savedMonth = -1
    private var savedYear = -1
    private var savedMillis: Long = 0

    private val _returnEvent by lazy { MutableLiveData<Event>() }
    val returnEvent: LiveData<Event> get() = _returnEvent

    private val _errorMessage by lazy { MutableLiveData<String>() }
    val addEventErrorMessage: LiveData<String> get() = _errorMessage

    private val _eventSaved by lazy { MutableLiveData<Boolean>() }
    val eventSaved: LiveData<Boolean> get() = _eventSaved

    private val _setDatePickerTime by lazy { MutableLiveData<String>() }
    val setDatePickerTime: LiveData<String> get() = _setDatePickerTime


    fun todaysDate() {
        _setDatePickerTime.value =
            TimeStampProcessing.todaysDate(TimeFlags.FULL)
    }


    fun updateSavedValues(value: Int, field: DatePickerFlags) {
        when (field) {
            DatePickerFlags.DAY -> savedDay = value
            DatePickerFlags.MONTH -> savedMonth = value.plus(1)
            DatePickerFlags.YEAR -> savedYear = value
        }
    }

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
                        _setDatePickerTime.value =
                            event.eventTime?.let {
                                TimeStampProcessing
                                    .transformSystemTime(it, TimeFlags.FULL)
                            }
                    }
            } catch (e: Exception) {
                _errorMessage.value = e.message.toString()
            }
        }
    }

}