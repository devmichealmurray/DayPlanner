package com.devmmurray.dayplanner.ui.viewmodel

import android.app.Application
import android.location.Address
import android.location.Location
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.devmmurray.dayplanner.R
import com.devmmurray.dayplanner.data.di.ApplicationModule
import com.devmmurray.dayplanner.data.di.DaggerViewModelComponent
import com.devmmurray.dayplanner.data.model.entity.CityStateEntity
import com.devmmurray.dayplanner.data.model.entity.CurrentWeatherEntity
import com.devmmurray.dayplanner.data.model.entity.HourlyForecastEntity
import com.devmmurray.dayplanner.data.model.entity.NewsEntity
import com.devmmurray.dayplanner.data.repository.ApiRepository
import com.devmmurray.dayplanner.data.usecases.*
import com.devmmurray.dayplanner.util.JsonProcessing
import com.devmmurray.dayplanner.util.flags.TimeFlags
import com.devmmurray.dayplanner.util.time.TimeStampProcessing
import kotlinx.coroutines.launch
import java.io.IOException
import javax.inject.Inject

open class SplashActivityViewModel(app: Application) : AndroidViewModel(app) {

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

    init {

        DaggerViewModelComponent.builder()
            .applicationModule(ApplicationModule(app))
            .build()
            .inject(this)
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


    /**
     * Networking calls to retrieve weather, city location, and news data
     */

    fun getNewData(location: Location?) {
        if (location?.latitude != null) {
            val searchDate = TimeStampProcessing.todaysDate(TimeFlags.NEWS_SEARCH_DATE)
            getWeatherFromOpenWeather(location.latitude, location.longitude)
            getGuardianNews(searchDate)
        } else {
            _errorMessage.value =
                getApplication<Application>().resources.getString(R.string.location_access_warning)
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
        }
    }

    fun deleteOldNews() {
        viewModelScope.launch {
            newsUseCases.deleteOldNews.invoke()
        }
    }

    private fun deleteCityInfo() {
        viewModelScope.launch {
            cityUseCases.deleteCityInfo.invoke()
        }
    }

    private fun addForecastsToDB(forecast: HourlyForecastEntity) {
        viewModelScope.launch {
            hourlyForecastsUseCases.addHourlyForecasts.invoke(forecast)
        }
    }

    private fun addCurrentWeatherToDB(weather: CurrentWeatherEntity) {
        viewModelScope.launch {
            weatherUseCases.addCurrentWeather.invoke(weather)
        }
    }

    private fun addNewsArticle(article: NewsEntity) {
        viewModelScope.launch {
            newsUseCases.addNewsArticle.invoke(article)
        }
    }

    fun getCityState(addresses: List<Address>) {
        try {
            if (!addresses.isNullOrEmpty()) {
                val city = addresses[0].locality
                val state = addresses[0].adminArea
                val cityStateLocation =
                    CityStateEntity(city = city, state = state)
                viewModelScope.launch {
                    cityUseCases.addCityState.invoke(cityStateLocation)
                }
            }
        } catch (e: Exception) {
            _errorMessage.value = e.message.toString()
        }
    }


}