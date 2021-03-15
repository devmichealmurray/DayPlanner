package com.devmmurray.dayplanner.ui.viewmodel

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.devmmurray.dayplanner.data.model.local.Event
import com.devmmurray.dayplanner.util.time.TimeFlags
import com.devmmurray.dayplanner.util.time.TimeStampProcessing
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch

class EventViewModel(app: Application) : HomeViewModel(app) {

    private val _returnEvent by lazy { MutableLiveData<Event>() }
    val returnEvent: LiveData<Event> get() = _returnEvent

    private val _eventErrorMessage by lazy { MutableLiveData<String>() }
    val eventErrorMessage: LiveData<String> get() = _eventErrorMessage

    fun getEventById(id: Long) {
        viewModelScope.launch {
            try {
                eventsUseCases.getEventById.invoke(id)
                    .flowOn(Dispatchers.IO)
                    .collect {
                        val event = it.toEventObject()
                        _returnEvent.value = event
                    }
            } catch (e: Exception) {
                _eventErrorMessage.value = e.message.toString()
            }
        }
    }

    fun deleteEvent(id: Long) {
        viewModelScope.launch {
            try {
                eventsUseCases.deleteEvent.invoke(id)
                eventsUseCases.getEvents.invoke(
                    TimeStampProcessing.todaysDate(TimeFlags.DATE_ID)
                )
            } catch (e: Exception) {
                _eventErrorMessage.value = e.message.toString()
            }
        }
    }
}