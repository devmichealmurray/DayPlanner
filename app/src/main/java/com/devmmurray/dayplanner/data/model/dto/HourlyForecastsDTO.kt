package com.devmmurray.dayplanner.data.model.dto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class HourlyForecastsDTO(
    @Json(name = "dt") val time: Long?,
    @Json(name = "temp") val hourlyTemp: Double?,
    @Json(name = "feels_like") val hourlyFeelsLike: Double?,
    @Json(name = "weather") val hourlyWeather: List<HourlyForecastWeatherDTO>?
)