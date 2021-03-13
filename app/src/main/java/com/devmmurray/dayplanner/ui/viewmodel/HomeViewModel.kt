package com.devmmurray.dayplanner.ui.viewmodel

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.devmmurray.dayplanner.data.model.entity.EventEntity
import com.devmmurray.dayplanner.data.model.entity.HourlyForecastEntity
import com.devmmurray.dayplanner.data.model.local.CityStateLocation
import com.devmmurray.dayplanner.data.model.local.CurrentWeather
import com.devmmurray.dayplanner.util.time.TimeFlags
import com.devmmurray.dayplanner.util.time.TimeStampProcessing
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch

private const val TAG = "Home View Model"

class HomeViewModel(app: Application) : SplashActivityViewModel(app) {

    private val _weatherProgress by lazy { MutableLiveData<Boolean>() }
    val weatherProgress: LiveData<Boolean> get() = _weatherProgress

    private val _forecastList by lazy { MutableLiveData<List<HourlyForecastEntity>>() }
    val forecastList: LiveData<List<HourlyForecastEntity>> get() = _forecastList

    private val _currentWeather by lazy { MutableLiveData<CurrentWeather>() }
    val currentWeather: LiveData<CurrentWeather> get() = _currentWeather

    private val _eventProgress by lazy { MutableLiveData<Boolean>() }
    val eventProgress: LiveData<Boolean> get() = _eventProgress

    private val _eventsList by lazy { MutableLiveData<List<EventEntity>>() }
    val eventsList: LiveData<List<EventEntity>> get() = _eventsList

    private val _cityState by lazy { MutableLiveData<CityStateLocation>() }
    val cityState: LiveData<CityStateLocation> get() = _cityState

    private val _errorMessage by lazy { MutableLiveData<String>() }
    val homeErrorMessage: LiveData<String> get() = _errorMessage


    private fun today() = TimeStampProcessing.todaysDate(TimeFlags.DATE_ID)

    /**
     *  Database Functions
     */

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

    fun getWeatherFromDB() {
        getHourlyForecastsFromDB()
        getCurrentWeatherFromDB()
    }

    private fun getHourlyForecastsFromDB() {
        viewModelScope.launch {
            _weatherProgress.value = true
            try {
                hourlyForecastsUseCases.getHourlyForecasts.invoke()
                    .flowOn(Dispatchers.IO)
                    .collect {
                        _forecastList.value = it
                        _weatherProgress.value = false
                    }
            } catch (e: Exception) {
                _errorMessage.value = e.message.toString()
                _weatherProgress.value = false
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
        val today = today()
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
                        _eventsList.value = eventsList
                        _eventProgress.value = false
                    }
            } catch (e: Exception) {
                _errorMessage.value = e.message.toString()
                _eventProgress.value = false
            }
        }
    }

    fun getCityState() {
        viewModelScope.launch {
            try {
                val cityState =
                    cityUseCases.getCityState.invoke()
                _cityState.value = cityState.toCityStateObject()
            } catch (e: Exception) {
                _errorMessage.value = e.message.toString()
            }
        }
    }

}