package com.devmmurray.dayplanner.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.devmmurray.dayplanner.data.model.entity.CurrentWeatherEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CurrentWeatherDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addCurrentWeather(weather: CurrentWeatherEntity)

    @Query("SELECT * FROM current_weather")
    fun getCurrentWeather(): Flow<CurrentWeatherEntity>

    @Query("DELETE FROM current_weather")
    suspend fun deleteOldWeather()
}