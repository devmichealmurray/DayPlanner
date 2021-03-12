package com.devmmurray.dayplanner.data.repository.dbrepos

import com.devmmurray.dayplanner.data.model.entity.CityStateEntity
import com.devmmurray.dayplanner.data.repository.datasource.CityStateLocationDataSource

class CityStateRepository(
    private val cityStateDataSource: CityStateLocationDataSource
) {
    suspend fun addCityState(cityState: CityStateEntity) =
        cityStateDataSource.addCityState(cityState)

    suspend fun getCityState() : CityStateEntity =
        cityStateDataSource.getCityState()

    suspend fun deleteCityInfo() =
        cityStateDataSource.deleteCityInfo()
}