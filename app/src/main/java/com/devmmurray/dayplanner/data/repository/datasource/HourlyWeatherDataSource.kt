package com.devmmurray.dayplanner.data.repository.datasource

import com.devmmurray.dayplanner.data.model.entity.HourlyForecastEntity
import kotlinx.coroutines.flow.Flow

interface HourlyWeatherDataSource {
    suspend fun addHourlyForecasts(forecast: HourlyForecastEntity)
    fun getHourlyForecasts() : Flow<List<HourlyForecastEntity>>
    suspend fun deleteOldHourlyForecasts()
}