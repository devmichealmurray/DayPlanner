package com.devmmurray.dayplanner.ui.viewmodel

import android.app.Application
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.devmmurray.dayplanner.data.model.entity.EventEntity
import com.devmmurray.dayplanner.data.model.entity.HourlyForecastEntity
import com.devmmurray.dayplanner.data.model.local.Event
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
        @BindingAdapter(value =["imageUrl"])
        fun bindImageUrl(view: ImageView, icon: String) {
            val url = "https://openweathermap.org/img/wn/$icon@2x.png"
            Picasso.get().load(url).into(view)
        }
    }

    private val _weatherProgress: MutableLiveData<Boolean> by lazy { MutableLiveData<Boolean>() }
    val weatherProgress: LiveData<Boolean> get() = _weatherProgress

    private val _eventProgress: MutableLiveData<Boolean> by lazy { MutableLiveData<Boolean>() }
    val eventProgress: LiveData<Boolean> get() = _eventProgress

    private val _forecastList by lazy { MutableLiveData<List<HourlyForecastEntity>>() }
    val forecastList: LiveData<List<HourlyForecastEntity>> get() = _forecastList

    private val _eventsList by lazy { MutableLiveData<List<Event>>() }
    val eventsList: LiveData<List<Event>> get() = _eventsList

    private val _errorMessage by lazy { MutableLiveData<String>() }
    val homeErrorMessage: LiveData<String> get() = _errorMessage




    fun getWeatherFromDB() {
        getWeatherEntitiesFromDB()
    }

    private fun getWeatherEntitiesFromDB() {
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

    fun getEventsFromDB() {
        val today = today()
        getEventEntitiesFromDB(today)
    }

    private fun getEventEntitiesFromDB(day: String) {
        _eventProgress.value = true
        viewModelScope.launch {
            try {
                val today = today()
                dbRepo.getEvents(today)
                    .flowOn(Dispatchers.IO)
                    .collect {
                        val events: MutableList<EventEntity> = mutableListOf()
                        val eventsList = events.sortedBy { it.dateId }
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

    private fun today() = TimeStampProcessing.todaysDate()

}

