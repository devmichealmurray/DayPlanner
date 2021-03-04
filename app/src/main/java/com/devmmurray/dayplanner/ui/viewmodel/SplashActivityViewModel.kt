package com.devmmurray.dayplanner.ui.viewmodel

import android.app.Application
import android.location.Geocoder
import android.location.Location
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.devmmurray.dayplanner.data.database.RoomDatabaseClient
import com.devmmurray.dayplanner.data.model.entity.CityStateEntity
import com.devmmurray.dayplanner.data.model.entity.HourlyForecastEntity
import com.devmmurray.dayplanner.data.repository.ApiRepository
import com.devmmurray.dayplanner.data.repository.DatabaseRepository
import com.devmmurray.dayplanner.util.JsonProcessing
import kotlinx.coroutines.launch
import java.util.*

private const val TAG = "MainActivityViewModel"

open class SplashActivityViewModel(application: Application) : AndroidViewModel(application) {
    val context = application

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
        val cityStateDAO = RoomDatabaseClient
            .getDbInstance(application).cityStateDAO()

        dbRepo = DatabaseRepository(hourlyForecastDAO, todoTaskDAO, eventDAO, cityStateDAO)

    }

    /**
     * Live Data
     */

    private val _errorMessage: MutableLiveData<String> by lazy { MutableLiveData<String>() }
    val errorMessage: LiveData<String> get() = _errorMessage

    private val _databaseNotReady: MutableLiveData<Boolean> by lazy { MutableLiveData<Boolean>() }
    val databaseNotReady: LiveData<Boolean> get() = _databaseNotReady


    /**
     * Networking calls to retrieve weather data
     */
    fun addLocation(location: Location?) {
        if (location?.latitude != null) {
            getWeatherFromOpenWeather(location.latitude, location.longitude)
            getCityState(location)
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
        _databaseNotReady.value = false
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

  private fun getCityState(location: Location) {
      try {
          val geoCoder = Geocoder(context, Locale.getDefault())
          val addresses = geoCoder.getFromLocation(location.latitude, location.longitude, 1)
          if (!addresses.isNullOrEmpty()) {
              val city = addresses[0].locality
              val state = addresses[0].adminArea
              val cityStateLocation =
                  CityStateEntity(city = city, state = state)
              viewModelScope.launch {
                  dbRepo.addCityState(cityStateLocation)
              }
          }
      } catch (e: Exception) {
          _errorMessage.value = e.message.toString()
      }


    }
}