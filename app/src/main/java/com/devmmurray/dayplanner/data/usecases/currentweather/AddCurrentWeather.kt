package com.devmmurray.dayplanner.data.usecases.currentweather

import com.devmmurray.dayplanner.data.model.entity.CurrentWeatherEntity
import com.devmmurray.dayplanner.data.repository.dbrepos.CurrentWeatherRepository

class AddCurrentWeather(
    private val currentWeatherRepository: CurrentWeatherRepository
) {
    suspend operator fun invoke(weather: CurrentWeatherEntity) =
        currentWeatherRepository.addCurrentWeather(weather)
}

class GetCurrentWeather(
    private val currentWeatherRepository: CurrentWeatherRepository
) {
    operator fun invoke() =
        currentWeatherRepository.getCurrentWeather()
}

class DeleteOldWeather(
    private val currentWeatherRepository: CurrentWeatherRepository
) {
    suspend operator fun invoke() =
        currentWeatherRepository.deleteOldWeather()
}