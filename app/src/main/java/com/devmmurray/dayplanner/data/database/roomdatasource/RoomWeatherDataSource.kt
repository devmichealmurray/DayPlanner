package com.devmmurray.dayplanner.data.database.roomdatasource

import android.content.Context
import com.devmmurray.dayplanner.data.database.RoomDatabaseClient
import com.devmmurray.dayplanner.data.model.entity.CurrentWeatherEntity
import com.devmmurray.dayplanner.data.repository.datasource.CurrentWeatherDataSource
import kotlinx.coroutines.flow.Flow

class RoomWeatherDataSource(context: Context) : CurrentWeatherDataSource {
    private val weatherDao = RoomDatabaseClient.getDbInstance(context).currentWeatherDAO()

    override suspend fun addCurrentWeather(weather: CurrentWeatherEntity) =
        weatherDao.addCurrentWeather(weather)

    override fun getCurrentWeather() : Flow<CurrentWeatherEntity> =
        weatherDao.getCurrentWeather()

    override suspend fun deleteOldWeather() =
        weatherDao.deleteOldWeather()
}