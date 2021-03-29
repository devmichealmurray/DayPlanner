package com.devmmurray.dayplanner.ui.viewmodel

import android.app.Application
import android.content.Intent
import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.devmmurray.dayplanner.data.model.local.Event
import com.devmmurray.dayplanner.util.flags.TimeFlags
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

    private val _shareIntent by lazy { MutableLiveData<Intent>() }
    val shareIntent: LiveData<Intent> get() = _shareIntent

    private val _mapsIntent by lazy { MutableLiveData<Intent>() }
    val mapsIntent: LiveData<Intent> get() = _mapsIntent

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

    fun createShareIntent(title: String?, location: String?, address: String?, time: String?) {
        val sendIntent: Intent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(
                Intent.EXTRA_TEXT,
                "Don't Forget About This Event! \n$title \n$location \n$address \n$time"
            )
            type = "text/plain"
        }
        _shareIntent.value = sendIntent
    }

    fun createMapsIntent(address: String?) {
        val gmmIntentUri = Uri.parse("geo:0,0?q=" + Uri.encode(address))
        val intent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
        intent.setPackage("com.google.android.apps.maps")
        _mapsIntent.value = intent
    }
}