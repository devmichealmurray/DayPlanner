package com.devmmurray.dayplanner.data.repository.datasource

import com.devmmurray.dayplanner.data.model.entity.CurrentWeatherEntity
import kotlinx.coroutines.flow.Flow

interface CurrentWeatherDataSource {
    suspend fun addCurrentWeather(weather: CurrentWeatherEntity)
    fun getCurrentWeather() : Flow<CurrentWeatherEntity>
    suspend fun deleteOldWeather()
}