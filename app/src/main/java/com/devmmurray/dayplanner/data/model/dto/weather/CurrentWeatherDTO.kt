package com.devmmurray.dayplanner.data.model.dto.weather

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class CurrentWeatherDTO(
    @Json(name = "dt") val time: Long?,
    @Json(name = "sunrise") val sunrise: Long?,
    @Json(name = "sunset") val sunset: Long?,
    @Json(name = "temp") val temp: Double?,
    @Json(name = "feels_like") val feels: Double?,
    @Json(name ="humidity") val humidity: Int?,
    @Json(name = "wind_speed") val windSpeed: Double?,
    @Json(name = "weather") val weather: List<CurrentWeatherDescriptionDTO>?
)