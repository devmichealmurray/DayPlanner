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

    fun prepareDate(day: Int, month: Int, year: Int) {
        _preparedDate.value = "$day/$month/$year"
        _dateDay.value = day
        _dateMonth.value = month
        _dateYear.value = year
    }

    fun prepareEvent(title: String, location: String, notes: String) {
        val event = EventEntity(
            title = title,
            location = location,
            notes = notes,
            dateDay = dateDay.value,
            dateMonth = dateMonth.value,
            dateYear = dateYear.value
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