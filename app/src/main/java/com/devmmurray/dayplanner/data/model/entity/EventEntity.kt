package com.devmmurray.dayplanner.data.model.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.devmmurray.dayplanner.data.model.local.Event

@Entity(tableName = "events")
class EventEntity(
    @PrimaryKey(autoGenerate = true)
    val uid: Long = 0L,
    @ColumnInfo(name = "title")
    val title: String?,
    @ColumnInfo(name = "date_day")
    val dateDay: Int?,
    @ColumnInfo(name = "date_month")
    val dateMonth: Int?,
    @ColumnInfo(name = "date_year")
    val dateYear: Int?,
    @ColumnInfo(name = "date_hour")
    val dateHour: Int?,
    @ColumnInfo(name = "date_minute")
    val dateMinute: Int?,
    @ColumnInfo(name = "time_period")
    val timePeriod: String?,
    @ColumnInfo(name = "location")
    val locationName: String?,
    @ColumnInfo(name = "address")
    val address: String?,
    @ColumnInfo(name = "notes")
    val notes: String?
) {

    fun toEventObject() = Event(
        uid,
        title,
        dateDay,
        dateMonth,
        dateYear,
        dateHour,
        dateMinute,
        timePeriod,
        locationName,
        address,
        notes)
}