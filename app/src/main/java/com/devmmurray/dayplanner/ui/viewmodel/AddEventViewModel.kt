package com.devmmurray.dayplanner.ui.viewmodel

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.devmmurray.dayplanner.data.model.entity.EventEntity
import kotlinx.coroutines.launch

class AddEventViewModel(application: Application): SplashActivityViewModel(application) {

    private val _preparedDate by lazy { MutableLiveData<String>() }
    val preparedDate: LiveData<String> get() = _preparedDate

    private val _dateDay by lazy { MutableLiveData<Int>() }
    val dateDay: LiveData<Int> get() = _dateDay

    private val _dateMonth by lazy { MutableLiveData<Int>() }
    val dateMonth: LiveData<Int> get() = _dateMonth

    private val _dateYear by lazy { MutableLiveData<Int>() }
    val dateYear: LiveData<Int> get() = _dateYear

    private val _dateHour by lazy { MutableLiveData<Int>() }
    val dateHour: LiveData<Int> get() = _dateHour

    private val _dateMinute by lazy { MutableLiveData<Int>() }
    val dateMinute: LiveData<Int> get() = _dateMinute

    private val _timePeriod by lazy { MutableLiveData<String>() }
    val timePeriod: LiveData<String> get() = _timePeriod

    fun prepareDate(day: Int, month: Int, year: Int, hour: Int, minute: Int) {
        _preparedDate.value = "$day/$month/$year"
        _dateDay.value = day
        _dateMonth.value = month
        _dateYear.value = year
        _dateHour.value = prepHour(hour)
        _dateMinute.value = minute
        _timePeriod.value = timePeriodChecker(hour)

    }

    private fun prepHour(hour: Int) = if (hour > 12) hour - 12 else hour

    private fun timePeriodChecker(time: Int) = if (time < 12) "AM" else "PM"


    fun prepareEvent(title: String, locationName: String, address: String, notes: String) {
        val event = EventEntity(
            title = title,
            locationName = locationName,
            address = address,
            notes = notes,
            dateDay = dateDay.value,
            dateMonth = dateMonth.value,
            dateYear = dateYear.value,
            dateHour = dateHour.value,
            dateMinute = dateMinute.value,
            timePeriod = timePeriod.value
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