package com.devmmurray.dayplanner.ui.viewmodel

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.devmmurray.dayplanner.data.model.entity.EventEntity
import com.devmmurray.dayplanner.util.time.TimeFlags
import com.devmmurray.dayplanner.util.time.TimeStampProcessing
import kotlinx.coroutines.launch

class AddEventViewModel(application: Application) : SplashActivityViewModel(application) {

    private val _preparedDate by lazy { MutableLiveData<String>() }
    val preparedDate: LiveData<String> get() = _preparedDate

    private val _dateInMillis by lazy { MutableLiveData<Long>() }
    val dateInMillis: LiveData<Long> get() = _dateInMillis

    private val _prepareDateId by lazy { MutableLiveData<String>() }
    val prepareDateId: LiveData<String> get() = _prepareDateId

    fun prepareDate(day: Int, month: Int, year: Int, hour: Int, minute: Int) {
        val dateString = "$day-$month-$year $hour:$minute"
        val dateInMillis = TimeStampProcessing.transformDateStringToMillis(dateString)
        val dateForAddEventFrag =
            TimeStampProcessing.transformSystemTime(dateInMillis, TimeFlags.FULL)
        _preparedDate.value = dateForAddEventFrag
        _prepareDateId.value = "$day-$month-$year"
    }

    private fun prepHour(hour: Int) = if (hour > 12) hour - 12 else hour

    fun prepareEvent(
        title: String,
        locationName: String,
        address: String,
        notes: String
    ) {
        val event = EventEntity(
            dateId = prepareDateId.value,
            title = title,
            eventTime = dateInMillis.value,
            locationName = locationName,
            address = address,
            notes = notes,
            )
        saveEventToDB(event)
    }

    private fun saveEventToDB(event: EventEntity) {
        viewModelScope.launch {
            try {

            } catch (e: Exception) {

            }
        }
    }
}