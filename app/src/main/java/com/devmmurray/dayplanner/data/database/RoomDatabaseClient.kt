package com.devmmurray.dayplanner.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.devmmurray.dayplanner.data.database.dao.*
import com.devmmurray.dayplanner.data.model.entity.*

const val DATABASE_SCHEMA_VERSION = 16
const val DB_NAME = "day-planner"

@Database(
    version = DATABASE_SCHEMA_VERSION,
    entities = [
        HourlyForecastEntity::class,
        CurrentWeatherEntity::class,
        TodoTaskEntity::class,
        EventEntity::class,
        CityStateEntity::class,
        NewsEntity::class
    ],
    exportSchema = false
)
abstract class RoomDatabaseClient : RoomDatabase() {

    abstract fun hourlyForecastsDAO(): HourlyForecastDAO
    abstract fun currentWeatherDAO(): CurrentWeatherDAO
    abstract fun todoTaskDAO(): TodoTaskDAO
    abstract fun eventDAO(): EventDAO
    abstract fun cityStateDAO(): CityStateDAO
    abstract fun newsDAO(): NewsDAO

    companion object {
        private var instance: RoomDatabaseClient? = null

        private fun createDatabase(context: Context): RoomDatabaseClient {
            return Room
                .databaseBuilder(context, RoomDatabaseClient::class.java, DB_NAME)
                .fallbackToDestructiveMigration()
                .build()
        }

        fun getDbInstance(context: Context): RoomDatabaseClient =
            (instance ?: createDatabase(context)).also {
                instance = it
            }
    }
}
