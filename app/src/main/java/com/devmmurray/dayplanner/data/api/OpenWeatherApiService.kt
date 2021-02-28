package com.devmmurray.dayplanner.data.api

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit
private const val BASE_URL = "https://api.openweathermap.org/"

object OpenWeatherApiService {

    private val okHttp = OkHttpClient.Builder()
        .connectTimeout(5, TimeUnit.MINUTES)
        .readTimeout(5, TimeUnit.MINUTES)
        .build()

    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(MoshiConverterFactory.create())
        .callFactory(okHttp)
        .build()

    val apiClient: OpenWeatherApi = retrofit.create(OpenWeatherApi::class.java)

}