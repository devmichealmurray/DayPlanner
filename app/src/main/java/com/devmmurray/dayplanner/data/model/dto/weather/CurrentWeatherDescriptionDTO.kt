package com.devmmurray.dayplanner.data.model.dto.weather

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class CurrentWeatherDescriptionDTO(
    @Json(name = "id") val currentId: Int?,
    @Json(name = "main") val mainDescription: String?,
    @Json(name = "description") val description: String?,
    @Json(name = "icon") val currentIcon: String?
)