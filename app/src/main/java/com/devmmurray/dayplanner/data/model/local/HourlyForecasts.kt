package com.devmmurray.dayplanner.data.model.local


data class HourlyForecasts(
    val uid: Long?,
    val time: String?,
    val hourlyTemp: String?,
    val hourlyFeelsLike: Int?,
    val hourlyWeather: HourlyForecastWeather?
)


