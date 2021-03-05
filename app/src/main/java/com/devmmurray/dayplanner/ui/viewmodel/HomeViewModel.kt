package com.devmmurray.dayplanner.ui.viewmodel

import android.app.Application
import android.util.Log
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.devmmurray.dayplanner.data.model.entity.EventEntity
import com.devmmurray.dayplanner.data.model.entity.HourlyForecastEntity
import com.devmmurray.dayplanner.data.model.local.CityStateLocation
import com.devmmurray.dayplanner.data.model.local.CurrentWeather
import com.devmmurray.dayplanner.util.time.TimeFlags
import com.devmmurray.dayplanner.util.time.TimeStampProcessing
import com.squareup.picasso.Picasso
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch

private const val TAG = "Home View Model"

class HomeViewModel(app: Application) : SplashActivityViewModel(app) {

    // Binding adapter used to bind photo url and load in RV
    companion object {
        @JvmStatic
        @BindingAdapter(value = ["imageUrl"])
        fun bindImageUrl(view: ImageView, icon: String) {
            val url = "https://openweathermap.org/img/wn/$icon@2x.png"
            Picasso.get()
                .load(url)
                .into(view)
        }
    }

    /**
     * Weather Live Data
     */

    private val _weatherProgress by lazy { MutableLiveData<Boolean>() }
    val weatherProgress: LiveData<Boolean> get() = _weatherProgress

    private val _forecastList by lazy { MutableLiveData<List<HourlyForecastEntity>>() }
    val forecastList: LiveData<List<HourlyForecastEntity>> get() = _forecastList

    private val _currentWeather by lazy { MutableLiveData<CurrentWeather>() }
    val currentWeather: LiveData<CurrentWeather> get() = _currentWeather

    /**
     * Event Live Data
     */

    private val _eventProgress by lazy { MutableLiveData<Boolean>() }
    val eventProgress: LiveData<Boolean> get() = _eventProgress

    private val _eventsList by lazy { MutableLiveData<List<EventEntity>>() }
    val eventsList: LiveData<List<EventEntity>> get() = _eventsList

    private val _cityState by lazy { MutableLiveData<CityStateLocation>() }
    val cityState: LiveData<CityStateLocation> get() = _cityState

    private val _errorMessage by lazy { MutableLiveData<String>() }
    val homeErrorMessage: LiveData<String> get() = _errorMessage


    /**
     *  Database Functions
     */

    fun getWeatherFromDB() {
        getHourlyForecastsFromDB()
        getCurrentWeatherFromDB()
    }

    private fun getHourlyForecastsFromDB() {
        viewModelScope.launch {
            _weatherProgress.value = true
            try {
                dbRepo.getHourlyForecasts()
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
                dbRepo.getCurrentWeather()
                    .flowOn(Dispatchers.IO)
                    .collect {
                        _currentWeather.value = it.toCurrentWeatherObject()


                        val test = it.toCurrentWeatherObject()
                        Log.d(TAG, "========== Wind Speed: ${test.windSpeed} ==============")
                        Log.d(TAG, "========== Humidity: ${test.humidity} ==============")
                        Log.d(TAG, "========== Main Description: ${test.currentWeatherDescription?.mainDescription} ===============")
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
                dbRepo.getEvents(day)
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

    private fun today() = TimeStampProcessing.todaysDate(TimeFlags.DATE_ID)

    fun getCityState() {
        viewModelScope.launch {
            try {
                val cityState = dbRepo.getCityState()
                _cityState.value = cityState.toCityStateObject()
            } catch (e: Exception) {
                _errorMessage.value = e.message.toString()
            }
        }
    }

}

