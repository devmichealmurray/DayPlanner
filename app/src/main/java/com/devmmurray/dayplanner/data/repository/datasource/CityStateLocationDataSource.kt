package com.devmmurray.dayplanner.data.repository.datasource

import com.devmmurray.dayplanner.data.model.entity.CityStateEntity

interface CityStateLocationDataSource {
    suspend fun addCityState(cityState: CityStateEntity)
    suspend fun getCityState() : CityStateEntity
    suspend fun deleteCityInfo()
}