package com.devmmurray.dayplanner.data.database.roomdatasource

import android.content.Context
import com.devmmurray.dayplanner.data.database.RoomDatabaseClient
import com.devmmurray.dayplanner.data.model.entity.HourlyForecastEntity
import com.devmmurray.dayplanner.data.repository.datasource.HourlyWeatherDataSource
import kotlinx.coroutines.flow.Flow

class RoomHourlyDataSource(context: Context) : HourlyWeatherDataSource {
    private val hourlyDao = RoomDatabaseClient.getDbInstance(context).hourlyForecastsDAO()

    override suspend fun addHourlyForecasts(forecast: HourlyForecastEntity) =
        hourlyDao.addHourlyForecasts(forecast)

    override fun getHourlyForecasts(): Flow<List<HourlyForecastEntity>> =
        hourlyDao.getHourlyForecasts()

    override suspend fun deleteOldHourlyForecasts() =
        hourlyDao.deleteOldHourlyForecasts()
}