package com.devmmurray.dayplanner.ui.viewmodel

import android.app.Application
import android.location.Geocoder
import android.location.Location
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.devmmurray.dayplanner.data.di.ApplicationModule
import com.devmmurray.dayplanner.data.di.DaggerViewModelComponent
import com.devmmurray.dayplanner.data.model.entity.CityStateEntity
import com.devmmurray.dayplanner.data.model.entity.CurrentWeatherEntity
import com.devmmurray.dayplanner.data.model.entity.HourlyForecastEntity
import com.devmmurray.dayplanner.data.model.entity.NewsEntity
import com.devmmurray.dayplanner.data.repository.ApiRepository
import com.devmmurray.dayplanner.data.usecases.*
import com.devmmurray.dayplanner.util.JsonProcessing
import com.devmmurray.dayplanner.util.time.TimeFlags
import com.devmmurray.dayplanner.util.time.TimeStampProcessing
import kotlinx.coroutines.launch
import java.io.IOException
import java.util.*
import javax.inject.Inject

private const val TAG = "SplashActivityViewModel"

open class SplashActivityViewModel(application: Application) : AndroidViewModel(application) {
    val context = application

    /**
     *  Set Up of Database
     */

    @Inject
    lateinit var cityUseCases: CityStateUseCases

    @Inject
    lateinit var weatherUseCases: CurrentWeatherUseCases

    @Inject
    lateinit var eventsUseCases: EventUseCases

    @Inject
    lateinit var hourlyForecastsUseCases: HourlyForecastsUseCases

    @Inject
    lateinit var newsUseCases: NewsUseCases

    @Inject
    lateinit var todoTasksUseCases: TodoTasksUseCases

//    val dbRepo: DatabaseRepository

    init {

        DaggerViewModelComponent.builder()
            .applicationModule(ApplicationModule(application))
            .build()
            .inject(this)

//        val hourlyForecastDAO = RoomDatabaseClient
//            .getDbInstance(application).hourlyForecastsDAO()
//        val currentWeatherDAO = RoomDatabaseClient
//            .getDbInstance(application).currentWeatherDAO()
//        val todoTaskDAO = RoomDatabaseClient
//            .getDbInstance(application).todoTaskDAO()
//        val eventDAO = RoomDatabaseClient
//            .getDbInstance(application).eventDAO()
//        val cityStateDAO = RoomDatabaseClient
//            .getDbInstance(application).cityStateDAO()
//        val newsDAO = RoomDatabaseClient
//            .getDbInstance(application).newsDAO()
//
//        dbRepo =
//            DatabaseRepository(
//                hourlyForecastDAO,
//                currentWeatherDAO,
//                todoTaskDAO,
//                eventDAO,
//                cityStateDAO,
//                newsDAO
//            )
    }


    /**
     * Live Data
     */

    private val _errorMessage: MutableLiveData<String> by lazy { MutableLiveData<String>() }
    val errorMessage: LiveData<String> get() = _errorMessage

    private val _ioException: MutableLiveData<Boolean> by lazy { MutableLiveData<Boolean>() }
    val ioException: LiveData<Boolean> get() = _ioException

    private val _databaseNotReady: MutableLiveData<Boolean> by lazy { MutableLiveData<Boolean>() }
    val databaseNotReady: LiveData<Boolean> get() = _databaseNotReady

    private val _toastMessage by lazy { MutableLiveData<String>() }
    val toastMessage: LiveData<String> get() = _toastMessage


    /**
     * Networking calls to retrieve weather, city location, and news data
     */

    fun getNewData(location: Location?) {
        if (location?.latitude != null) {
            val searchDate = TimeStampProcessing.todaysDate(TimeFlags.NEWS_SEARCH_DATE)
            getWeatherFromOpenWeather(location.latitude, location.longitude)
            getCityState(location)
            getGuardianNews(searchDate)
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

                    val currentWeather = JsonProcessing.parseForCurrentWeather(result)
                    addCurrentWeatherToDB(currentWeather)
                    _databaseNotReady.value = false
                } else {
                    _errorMessage.value = result.errorBody().toString()
                }
            } catch (e: Exception) {
                _errorMessage.value = e.message.toString()
            }
        }

    }

    private fun getGuardianNews(fromDate: String) {
        viewModelScope.launch {
            try {
                val result = ApiRepository.getNews(fromDate)
                if (result.isSuccessful) {
                    val news = JsonProcessing.parseNewsArticles(result)
                    news.forEach {
                        addNewsArticle(it)
                    }
                } else {
                    _errorMessage.value = result.errorBody().toString()
                }
            } catch (e: IOException) {
                _ioException.value = true
            } catch (e: Exception) {
                _errorMessage.value = e.message.toString()
            }
        }
    }


    /**
     *  Database Functions
     */

    fun deleteOldData() {
        deleteOldWeatherData()
        deleteOldNews()
        deleteCityInfo()
    }

    private fun deleteOldWeatherData() {
        viewModelScope.launch {
            hourlyForecastsUseCases.deleteOldHourlyForecasts.invoke()
            weatherUseCases.deleteOldWeather.invoke()
//            dbRepo.deleteOldHourlyForecasts()
//            dbRepo.deleteOldWeather()
        }
    }

    fun deleteOldNews() {
        viewModelScope.launch {
            newsUseCases.deleteOldNews.invoke()
//            dbRepo.deleteOldNews()
        }
    }

    private fun deleteCityInfo() {
        viewModelScope.launch {
            cityUseCases.deleteCityInfo.invoke()
//            dbRepo.deleteCityInfo()
        }
    }

    private fun addForecastsToDB(forecast: HourlyForecastEntity) {
        viewModelScope.launch {
            hourlyForecastsUseCases.addHourlyForecasts.invoke(forecast)
//            dbRepo.addHourlyForecasts(forecast)
        }
    }

    private fun addCurrentWeatherToDB(weather: CurrentWeatherEntity) {
        viewModelScope.launch {
            weatherUseCases.addCurrentWeather.invoke(weather)
//            dbRepo.addCurrentWeather(weather)
        }
    }

    private fun addNewsArticle(article: NewsEntity) {
        viewModelScope.launch {
            newsUseCases.addNewsArticle.invoke(article)
//            dbRepo.addNewsArticle(article)
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
                    cityUseCases.addCityState.invoke(cityStateLocation)
//                    dbRepo.addCityState(cityStateLocation)
                }
            }
        } catch (e: Exception) {
            _errorMessage.value = e.message.toString()
        }
    }


}