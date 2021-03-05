package com.devmmurray.dayplanner.util

import com.devmmurray.dayplanner.data.model.dto.WeatherDTO
import com.devmmurray.dayplanner.data.model.entity.CurrentWeatherDescriptionEntity
import com.devmmurray.dayplanner.data.model.entity.CurrentWeatherEntity
import com.devmmurray.dayplanner.data.model.entity.HourlyForecastEntity
import com.devmmurray.dayplanner.data.model.entity.HourlyForecastWeatherEntity
import com.devmmurray.dayplanner.util.time.TimeFlags
import com.devmmurray.dayplanner.util.time.TimeStampProcessing
import retrofit2.Response

private const val TAG = "JSON Processing"

object JsonProcessing {

    fun parseForCurrentWeather(result: Response<WeatherDTO>): CurrentWeatherEntity {
        val currentResponse = result.body()?.current
        var currentWeatherDescription: CurrentWeatherDescriptionEntity? = null

        currentResponse?.weather?.forEach {
            currentWeatherDescription = CurrentWeatherDescriptionEntity(
                currentId = it.currentId,
                mainDescription = it.mainDescription,
                description = it.description?.capitalize(),
                currentIcon = it.currentIcon
            )
        }

        return CurrentWeatherEntity(
            time = currentResponse?.time?.let { utcTime ->
                TimeStampProcessing.transformUTCTime(utcTime, TimeFlags.FULL)
            },
            sunrise = "Sunrise:  " + currentResponse?.sunrise?.let {
                TimeStampProcessing.transformUTCTime(it, TimeFlags.HOUR)
            },
            sunset = "Sunset:  " + currentResponse?.sunset?.let {
                TimeStampProcessing.transformUTCTime(it, TimeFlags.HOUR) },
            temp = "Current Temperature:  " + currentResponse?.temp?.toInt().toString() + "\u00B0",
            feels = "Feels Like:  " + currentResponse?.feels?.toInt().toString() + "\u00B0",
            humidity = "Current Humidity:  " + currentResponse?.humidity.toString() + "%",
            windSpeed = "Wind Speed:  " + currentResponse?.windSpeed.toString() + "mph",
            currentWeatherDescription = currentWeatherDescription

        )
    }


    fun parseForHourlyForecast(result: Response<WeatherDTO>): ArrayList<HourlyForecastEntity> {
        val hourlyForecastList = ArrayList<HourlyForecastEntity>()
        var hourlyForecastWeather: HourlyForecastWeatherEntity? = null

        result.body()?.hourlyForecasts?.forEach {
            it.hourlyWeather?.forEach { hourly ->
                hourlyForecastWeather = HourlyForecastWeatherEntity(
                    hourlyId = hourly.hourlyId,
                    mainForecast = hourly.mainForecast,
                    forecastDescription = hourly.forecastDescription?.capitalize(),
                    forecastIcon = hourly.forecastIcon
                )
                val hourlyForecast = HourlyForecastEntity(
                    time = it.time?.let { utcTime ->
                        TimeStampProcessing.transformUTCTime(utcTime, TimeFlags.HOUR)
                    },
                    hourlyTemp = it.hourlyTemp?.toInt().toString() + "\u00B0",
                    hourlyFeelsLike = it.hourlyFeelsLike?.toInt(),
                    hourlyWeather = hourlyForecastWeather
                )
                hourlyForecastList.add(hourlyForecast)
            }

        }

        return hourlyForecastList
    }

}