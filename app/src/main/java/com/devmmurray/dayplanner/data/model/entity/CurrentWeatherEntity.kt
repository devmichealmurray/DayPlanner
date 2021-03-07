package com.devmmurray.dayplanner.data.model.entity

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.devmmurray.dayplanner.data.model.local.CurrentWeather
import com.devmmurray.dayplanner.data.model.local.CurrentWeatherDescription

@Entity(tableName = "current_weather")
class CurrentWeatherEntity(
    @PrimaryKey(autoGenerate = true)
    val uid: Long = 0L,
    @ColumnInfo(name = "time")
    val time: String?,
    @ColumnInfo(name = "sunrise")
    val sunrise: String?,
    @ColumnInfo(name = "sunset")
    val sunset: String?,
    @ColumnInfo(name = "temp")
    val temp: String?,
    @ColumnInfo(name = "feels")
    val feels: String?,
    @ColumnInfo(name = "humidity")
    val humidity: String?,
    @ColumnInfo(name = "wind_speed")
    val windSpeed: String?,
    @Embedded
    val currentWeatherDescription: CurrentWeatherDescriptionEntity?
) {
    fun toCurrentWeatherObject() = CurrentWeather(
        time, sunrise, sunset, temp, feels, humidity, windSpeed,
        currentWeatherDescription?.toWeatherDescriptionObject()
    )
}

@Entity
class CurrentWeatherDescriptionEntity(
    @ColumnInfo(name = "current_id")
    val currentId: Int?,
    @ColumnInfo(name = "main_description")
    val mainDescription: String?,
    @ColumnInfo(name = "description")
    val description: String?,
    @ColumnInfo(name = "current_icon")
    val currentIcon: String?
) {
    fun toWeatherDescriptionObject() = CurrentWeatherDescription(
        currentId, mainDescription, description, currentIcon
    )
}