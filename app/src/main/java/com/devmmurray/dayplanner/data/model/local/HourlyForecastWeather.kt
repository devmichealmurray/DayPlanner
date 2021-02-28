package com.devmmurray.dayplanner.data.model.local

data class HourlyForecastWeather(
    val hourlyId: Int?,
    val mainForecast: String?,
    val forecastDescription: String?,
    val forecastIcon: String?
)