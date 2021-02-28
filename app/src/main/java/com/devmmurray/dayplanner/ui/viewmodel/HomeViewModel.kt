package com.devmmurray.dayplanner.ui.viewmodel

import android.app.Application
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.devmmurray.dayplanner.data.model.entity.HourlyForecastEntity
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

    private val _progress: MutableLiveData<Boolean> by lazy { MutableLiveData<Boolean>() }
    val progress: LiveData<Boolean> get() = _progress

    private val _forecastList by lazy { MutableLiveData<List<HourlyForecastEntity>>() }
    val forecastList: LiveData<List<HourlyForecastEntity>> get() = _forecastList

    private val _errorMessage by lazy { MutableLiveData<String>() }
    val homeErrorMessage: LiveData<String> get() = _errorMessage


    fun getWeatherFromDB() {
        getWeatherEntitiesFromDB()
    }

    private fun getWeatherEntitiesFromDB() {
        viewModelScope.launch {
            _progress.value = true
            try {
                dbRepo.getHourlyForecasts()
                    .flowOn(Dispatchers.IO)
                    .collect {
                        _forecastList.value = it
                        _progress.value = false
                    }
            } catch (e: Exception) {
                _errorMessage.value = e.message.toString()
                _progress.value = false
            }

        }

    }

}

