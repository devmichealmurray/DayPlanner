package com.devmmurray.dayplanner.data.model.entity

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.devmmurray.dayplanner.data.model.local.HourlyForecastWeather
import com.devmmurray.dayplanner.data.model.local.HourlyForecasts

@Entity(tableName = "hourly_forecasts")
class HourlyForecastEntity(
    @PrimaryKey(autoGenerate = true)
    var uid: Long = 0L,
    @ColumnInfo(name = "time")
    val time: String?,
    @ColumnInfo(name = "temp")
    val hourlyTemp: String?,
    @ColumnInfo(name = "feels")
    val hourlyFeelsLike: Int?,
    @Embedded
    val hourlyWeather: HourlyForecastWeatherEntity?

) {
    fun toHourlyForecastObject() = HourlyForecasts(
        uid, time, hourlyTemp, hourlyFeelsLike, hourlyWeather?.toHourlyForecastWeatherObject()
    )
}

@Entity
class HourlyForecastWeatherEntity(
    val hourlyId: Int?,
    val mainForecast: String?,
    val forecastDescription: String?,
    val forecastIcon: String?
) {
    fun toHourlyForecastWeatherObject() = HourlyForecastWeather(
        hourlyId, mainForecast, forecastDescription, forecastIcon
    )
}
