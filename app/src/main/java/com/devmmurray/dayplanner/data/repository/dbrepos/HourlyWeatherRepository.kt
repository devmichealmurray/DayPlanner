package com.devmmurray.dayplanner.data.repository.dbrepos

import com.devmmurray.dayplanner.data.model.entity.HourlyForecastEntity
import com.devmmurray.dayplanner.data.repository.datasource.HourlyWeatherDataSource

class HourlyWeatherRepository(
    private val hourlyForecastDataSource: HourlyWeatherDataSource) {

    suspend fun addHourlyForecasts(forecast: HourlyForecastEntity) =
        hourlyForecastDataSource.addHourlyForecasts(forecast)

    fun getHourlyForecasts() =
        hourlyForecastDataSource.getHourlyForecasts()

    suspend fun deleteOldHourlyForecasts() =
        hourlyForecastDataSource.deleteOldHourlyForecasts()
}