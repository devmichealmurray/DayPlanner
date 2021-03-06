package com.devmmurray.dayplanner.ui.viewmodel

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.devmmurray.dayplanner.data.model.entity.EventEntity
import com.devmmurray.dayplanner.data.model.local.CurrentWeather
import com.devmmurray.dayplanner.data.model.local.Event
import com.devmmurray.dayplanner.data.model.local.HourlyForecasts
import com.devmmurray.dayplanner.util.flags.TimeFlags
import com.devmmurray.dayplanner.util.time.TimeStampProcessing
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch

open class HomeViewModel(app: Application) : SplashActivityViewModel(app) {

    private val _forecastList by lazy { MutableLiveData<List<HourlyForecasts>>() }
    val forecastList: LiveData<List<HourlyForecasts>> get() = _forecastList

    private val _currentWeather by lazy { MutableLiveData<CurrentWeather>() }
    val currentWeather: LiveData<CurrentWeather> get() = _currentWeather

    private val _eventProgress by lazy { MutableLiveData<Boolean>() }
    val eventProgress: LiveData<Boolean> get() = _eventProgress

    private val _eventsList by lazy { MutableLiveData<List<Event>>() }
    val eventsList: LiveData<List<Event>> get() = _eventsList

    private val _cityState by lazy { MutableLiveData<String>() }
    val cityState: LiveData<String> get() = _cityState

    private val _errorMessage by lazy { MutableLiveData<String>() }
    val homeErrorMessage: LiveData<String> get() = _errorMessage


    /**
     *  Database Functions; Weather, Events, City & State
     */

    fun getWeatherFromDB() {
        getHourlyForecastsFromDB()
        getCurrentWeatherFromDB()
    }

    private fun getHourlyForecastsFromDB() {
        viewModelScope.launch {
            try {
                hourlyForecastsUseCases.getHourlyForecasts.invoke()
                    .flowOn(Dispatchers.IO)
                    .collect { list ->
                        val forecasts = list.map { it.toHourlyForecastObject() }
                        _forecastList.value = forecasts
                    }
            } catch (e: Exception) {
                _errorMessage.value = e.message.toString()
            }
        }

    }

    private fun getCurrentWeatherFromDB() {
        viewModelScope.launch {
            try {
                weatherUseCases.getCurrentWeather.invoke()
                    .flowOn(Dispatchers.IO)
                    .collect {
                        _currentWeather.value = it.toCurrentWeatherObject()
                    }
            } catch (e: Exception) {
                _errorMessage.value = e.message.toString()
            }
        }
    }


    fun getEventsFromDB() {
        val today = TimeStampProcessing.todaysDate(TimeFlags.DATE_ID)
        getEventEntitiesFromDB(today)
    }

    private fun getEventEntitiesFromDB(day: String) {
        _eventProgress.value = true
        viewModelScope.launch {
            try {
                eventsUseCases.getEvents.invoke(day)
                    .flowOn(Dispatchers.IO)
                    .collect { dbList ->
                        val events: MutableList<EventEntity> = dbList.toMutableList()
                        val eventsList = events
                            .sortedBy { it.eventTime }
                            .map { it.toEventObject() }
                        _eventsList.value = eventsList
                        _eventProgress.value = false
                    }
            } catch (e: Exception) {
                _errorMessage.value = e.message.toString()
                _eventProgress.value = false
            }
        }
    }

    // Switch to change what events list displays
    fun changeEventsList(isChecked: Boolean) {
        if (isChecked) {
            _eventProgress.value = true
            viewModelScope.launch {
                try {
                    eventsUseCases.getAllEvents.invoke()
                        .flowOn(Dispatchers.IO)
                        .collect { dbList ->
                            val events: MutableList<EventEntity> = dbList.toMutableList()
                            val eventsList = events
                                .sortedBy { it.eventTime }
                                .map { it.toEventObject() }
                            _eventsList.value = eventsList
                            _eventProgress.value = false
                        }
                } catch (e: Exception) {
                    _errorMessage.value = e.message.toString()
                    _eventProgress.value = false
                }
            }
        } else {
            getEventsFromDB()
        }
    }

    fun deleteOldEvents(date: Long) {
        viewModelScope.launch {
            eventsUseCases.getAllEvents.invoke()
                .flowOn(Dispatchers.IO)
                .collect { list ->
                    list.forEach { event ->
                        event.eventTime?.let { eventTime ->
                            if (eventTime < date) {
                                eventsUseCases.deleteEvent.invoke(event.uid)
                            }
                        }
                    }
                }
        }
    }


    // Gets current location stored in database
    fun getCityState() {
        viewModelScope.launch {
            try {
                val cityStateEntity =
                    cityUseCases.getCityState.invoke()
                val cityState = cityStateEntity.toCityStateObject()
                _cityState.value = "${cityState.city}, ${cityState.state}"
            } catch (e: Exception) {
                _errorMessage.value = e.message.toString()
            }
        }
    }

}