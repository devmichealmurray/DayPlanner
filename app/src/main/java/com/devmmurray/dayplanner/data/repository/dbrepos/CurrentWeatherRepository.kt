package com.devmmurray.dayplanner.data.repository.dbrepos

import com.devmmurray.dayplanner.data.model.entity.CurrentWeatherEntity
import com.devmmurray.dayplanner.data.repository.datasource.CurrentWeatherDataSource

class CurrentWeatherRepository(
    private val currentWeatherDataSource: CurrentWeatherDataSource
) {
    suspend fun addCurrentWeather(weather: CurrentWeatherEntity) =
        currentWeatherDataSource.addCurrentWeather(weather)

    fun getCurrentWeather() =
        currentWeatherDataSource.getCurrentWeather()

    suspend fun deleteOldWeather() =
        currentWeatherDataSource.deleteOldWeather()
}