package com.devmmurray.dayplanner.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.devmmurray.dayplanner.data.model.entity.HourlyForecastEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface HourlyForecastDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addHourlyForecasts(forecast: HourlyForecastEntity)

    @Query("SELECT * FROM hourly_forecasts")
    fun getHourlyForecasts(): Flow<List<HourlyForecastEntity>>

    @Query("DELETE FROM hourly_forecasts")
    suspend fun deleteOldHourlyForecasts()
}