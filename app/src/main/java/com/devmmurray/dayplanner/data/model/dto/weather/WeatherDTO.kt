package com.devmmurray.dayplanner.data.model.dto.weather

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class WeatherDTO(
    @Json(name = "timezone_offset") val timeZoneOffset: Long?,
    @Json(name = "current") val current: CurrentWeatherDTO?,
    @Json(name = "hourly") val hourlyForecasts: List<HourlyForecastsDTO>?
)