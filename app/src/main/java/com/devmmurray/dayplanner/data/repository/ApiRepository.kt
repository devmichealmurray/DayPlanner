package com.devmmurray.dayplanner.data.repository

import com.devmmurray.dayplanner.data.api.guardian.GuardianApiService
import com.devmmurray.dayplanner.data.api.openweather.OpenWeatherApiService
import com.devmmurray.dayplanner.data.model.dto.news.NewsDTO
import com.devmmurray.dayplanner.data.model.dto.weather.WeatherDTO
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

    suspend fun getNews(
        fromDate: String,
    ): Response<NewsDTO> {
        return GuardianApiService.apiClient.guardianCall(
            pageSize = "24",
            date = fromDate,
            apiKey = "ed41fb6c-28a3-4bd2-989c-ebc34d98f916"
        )
    }
}