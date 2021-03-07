package com.devmmurray.dayplanner.data.model.local

data class CurrentWeather(
    val time: String?,
    val sunrise: String?,
    val sunset: String?,
    val temp: String?,
    val feels: String?,
    val humidity: String?,
    val windSpeed: String?,
    val currentWeatherDescription: CurrentWeatherDescription?
)