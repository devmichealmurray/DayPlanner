package com.devmmurray.dayplanner.util

import com.devmmurray.dayplanner.data.model.dto.WeatherDTO
import com.devmmurray.dayplanner.data.model.entity.HourlyForecastEntity
import com.devmmurray.dayplanner.data.model.entity.HourlyForecastWeatherEntity
import com.devmmurray.dayplanner.util.time.TimeFlags
import com.devmmurray.dayplanner.util.time.TimeStampProcessing
import retrofit2.Response

private const val TAG = "JSON Processing"

object JsonProcessing {
    fun parseForHourlyForecast(result: Response<WeatherDTO>): ArrayList<HourlyForecastEntity> {
        val hourlyForecastList = ArrayList<HourlyForecastEntity>()
        var hourlyForecastWeather: HourlyForecastWeatherEntity? = null

        result.body()?.hourlyForecasts?.forEach {
            it.hourlyWeather?.forEach { hourly ->
                hourlyForecastWeather = HourlyForecastWeatherEntity(
                    hourlyId = hourly.hourlyId,
                    mainForecast = hourly.mainForecast,
                    forecastDescription = hourly.forecastDescription,
                    forecastIcon = hourly.forecastIcon
                )
                val hourlyForecast = HourlyForecastEntity(
                    time = it.time?.let { utcTime ->
                        TimeStampProcessing.transformUTCTime(utcTime, TimeFlags.HOUR)
                    },
                    hourlyTemp = it.hourlyTemp?.toInt(),
                    hourlyFeelsLike = it.hourlyFeelsLike?.toInt(),
                    hourlyWeather = hourlyForecastWeather
                )
                hourlyForecastList.add(hourlyForecast)
            }

        }

        return hourlyForecastList
    }

}