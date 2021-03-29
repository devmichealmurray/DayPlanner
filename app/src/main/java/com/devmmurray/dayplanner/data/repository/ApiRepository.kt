package com.devmmurray.dayplanner.data.repository

import com.devmmurray.dayplanner.data.api.guardian.GuardianApiService
import com.devmmurray.dayplanner.data.api.openweather.OpenWeatherApiService
import com.devmmurray.dayplanner.data.model.dto.news.NewsDTO
import com.devmmurray.dayplanner.data.model.dto.weather.WeatherDTO
import com.devmmurray.dayplanner.util.Constants
import retrofit2.Response



object ApiRepository {

    suspend fun getWeatherOneCall(
        lat: Double,
        lon: Double,
        units: String): Response<WeatherDTO> {
        return OpenWeatherApiService.apiClient.weatherOneCall(
            lat = lat,
            lon = lon,
            units = units,
            apiKey = Constants.WEATHER_API_KEY
        )
    }

    suspend fun getNews(
        fromDate: String
    ): Response<NewsDTO> {
        return GuardianApiService.apiClient.guardianCall(
            pageSize = "24",
            date = fromDate,
            apiKey = Constants.NEWS_API_KEY
        )
    }

    suspend fun searchGuardian(
        searchTerms: String
    ) : Response<NewsDTO> {
        return GuardianApiService.apiClient.guardianSearchCall(
            pageSize = "24",
            searchTerms = searchTerms,
            apiKey = Constants.NEWS_API_KEY
        )
    }
}