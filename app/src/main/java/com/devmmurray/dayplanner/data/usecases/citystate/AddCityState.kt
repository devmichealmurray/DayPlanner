package com.devmmurray.dayplanner.data.usecases.citystate

import com.devmmurray.dayplanner.data.model.entity.CityStateEntity
import com.devmmurray.dayplanner.data.repository.dbrepos.CityStateRepository

class AddCityState(private val cityStateRepository: CityStateRepository) {
    suspend operator fun invoke(cityState: CityStateEntity) =
        cityStateRepository.addCityState(cityState)
}

class GetCityState(private val cityStateRepository: CityStateRepository) {
    suspend operator fun invoke() =
        cityStateRepository.getCityState()
}

class DeleteCityInfo(private val cityStateRepository: CityStateRepository) {
    suspend operator fun invoke() =
        cityStateRepository.deleteCityInfo()
}