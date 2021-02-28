package com.devmmurray.dayplanner.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.devmmurray.dayplanner.data.model.entity.HourlyForecastEntity
import com.devmmurray.dayplanner.data.model.entity.TodoTaskEntity

const val DATABASE_SCHEMA_VERSION = 4
const val DB_NAME = "day-planner"

@Database(
    version = DATABASE_SCHEMA_VERSION,
    entities = [
        HourlyForecastEntity::class, TodoTaskEntity::class
    ],
    exportSchema = false
)

abstract class RoomDatabaseClient : RoomDatabase() {

    abstract fun hourlyForecastsDAO(): HourlyForecastDAO
    abstract fun todoTaskDAO(): TodoTaskDAO

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
