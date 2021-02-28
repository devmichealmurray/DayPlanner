package com.devmmurray.dayplanner.data.repository

import com.devmmurray.dayplanner.data.api.OpenWeatherApiService
import com.devmmurray.dayplanner.data.model.dto.WeatherDTO
import retrofit2.Response

private const val API_KEY = "6f2f1d47ba30bfd187ed7ec88224312c"

object ApiRepository {

    suspend fun getWeatherOneCall(
        lat: Double,
        lon: Double,
        units: String): Response<WeatherDTO> {
        return OpenWeatherApiService.apiClient.weatherOneCall(
            lat = lat,
            lon = lon,
            units = units,
            apiKey = "6f2f1d47ba30bfd187ed7ec88224312c"
        )
    }
}