package com.devmmurray.dayplanner.data.model.dto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class HourlyForecastWeatherDTO(
    @Json(name = "id") val hourlyId: Int?,
    @Json(name = "main") val mainForecast: String?,
    @Json(name = "description") val forecastDescription: String?,
    @Json(name = "icon") val forecastIcon: String?
)