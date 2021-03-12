package com.devmmurray.dayplanner.data.database.roomdatasource

import android.content.Context
import com.devmmurray.dayplanner.data.database.RoomDatabaseClient
import com.devmmurray.dayplanner.data.model.entity.CityStateEntity
import com.devmmurray.dayplanner.data.repository.datasource.CityStateLocationDataSource

class RoomCityStateDataSource(context: Context) : CityStateLocationDataSource {
    private val cityDao = RoomDatabaseClient.getDbInstance(context).cityStateDAO()

    override suspend fun addCityState(cityState: CityStateEntity) =
        cityDao.addCityState(cityState)

    override suspend fun getCityState() : CityStateEntity =
        cityDao.getCityState()

    override suspend fun deleteCityInfo() =
        cityDao.deleteCityInfo()
}