package com.devmmurray.dayplanner.ui.viewmodel

import android.app.Application
import android.location.Location
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.devmmurray.dayplanner.data.database.RoomDatabaseClient
import com.devmmurray.dayplanner.data.model.entity.HourlyForecastEntity
import com.devmmurray.dayplanner.data.repository.ApiRepository
import com.devmmurray.dayplanner.data.repository.DatabaseRepository
import com.devmmurray.dayplanner.util.JsonProcessing
import kotlinx.coroutines.launch

private const val TAG = "MainActivityViewModel"

open class SplashActivityViewModel(application: Application) : AndroidViewModel(application) {

    /**
     *  Set Up of Database
     */
    val dbRepo: DatabaseRepository

    init {

        val hourlyForecastDAO = RoomDatabaseClient
            .getDbInstance(application).hourlyForecastsDAO()
        val todoTaskDAO = RoomDatabaseClient
            .getDbInstance(application).todoTaskDAO()
        val eventDAO = RoomDatabaseClient
            .getDbInstance(application).eventDAO()

        dbRepo = DatabaseRepository(hourlyForecastDAO, todoTaskDAO, eventDAO)

    }

    /**
     * Live Data
     */

    private val _errorMessage: MutableLiveData<String> by lazy { MutableLiveData<String>() }
    val errorMessage: LiveData<String> get() = _errorMessage

    private val _databaseReady: MutableLiveData<Boolean> by lazy { MutableLiveData<Boolean>() }
    val databaseReady: LiveData<Boolean> get() = _databaseReady


    /**
     * Networking calls to retrieve weather data
     */
    fun addLocation(location: Location?) {
        if (location?.latitude != null) {
            getWeatherFromOpenWeather(location.latitude, location.longitude)
        } else {
            _errorMessage.value = "Could Not Access Location"
        }
    }

    private fun getWeatherFromOpenWeather(lat: Double, lon: Double, units: String = "imperial") {
        viewModelScope.launch {
            try {
                val result = ApiRepository.getWeatherOneCall(lat, lon, units)
                if (result.isSuccessful) {
                    val hourlyForecast = JsonProcessing.parseForHourlyForecast(result)
                    hourlyForecast.forEach { addForecastsToDB(it) }

                } else {
                    _errorMessage.value = result.errorBody().toString()
                }
            } catch (e: Exception) {
                _errorMessage.value = e.message.toString()
            }
        }
        _databaseReady.value = false
    }

    /**
     *  Database Functions
     */

    fun deleteOldWeatherData() {
        viewModelScope.launch {
            dbRepo.deleteOldHourlyForecasts()
        }
    }

    private fun addForecastsToDB(forecast: HourlyForecastEntity) {
       viewModelScope.launch {
           dbRepo.addHourlyForecasts(forecast)
       }
    }



}