package com.devmmurray.dayplanner.data.model.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.devmmurray.dayplanner.data.model.local.CityStateLocation

@Entity(tableName = "city_state")
class CityStateEntity(
    @PrimaryKey(autoGenerate = true)
    val uid: Long = 0L,
    @ColumnInfo(name = "city")
    val city: String?,
    @ColumnInfo(name = "state")
    val state: String?
) {
    fun toCityStateObject() = CityStateLocation(city, state)
}