package com.devmmurray.dayplanner.data.api.openweather

import com.devmmurray.dayplanner.data.model.dto.weather.WeatherDTO
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Call to Open Weather to get forecast by geo location
 * https://api.openweathermap.org/data/2.5/onecall?lat={lat}&lon={lon}&units={units}&exclude=minutely&appid={API key}
 */

interface OpenWeatherApi {

    @GET("data/2.5/onecall")
    suspend fun weatherOneCall(
        @Query("lat") lat: Double,
        @Query("lon") lon: Double,
        @Query("units") units: String,
        @Query("apiKey") apiKey: String

    ): Response<WeatherDTO>

}