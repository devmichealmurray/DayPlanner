package com.devmmurray.dayplanner.data.usecases.hourlyforecasts

import com.devmmurray.dayplanner.data.model.entity.HourlyForecastEntity
import com.devmmurray.dayplanner.data.repository.dbrepos.HourlyWeatherRepository

class AddHourlyForecasts(
    private val hourlyWeatherRepository: HourlyWeatherRepository
) {
    suspend operator fun invoke(forecast: HourlyForecastEntity) =
        hourlyWeatherRepository.addHourlyForecasts(forecast)
}

class GetHourlyForecasts(
    private val hourlyWeatherRepository: HourlyWeatherRepository
) {
    operator fun invoke() =
        hourlyWeatherRepository.getHourlyForecasts()
}

class DeleteOldHourlyForecasts(
    private val hourlyWeatherRepository: HourlyWeatherRepository
) {
    suspend operator fun invoke() =
        hourlyWeatherRepository.deleteOldHourlyForecasts()
}