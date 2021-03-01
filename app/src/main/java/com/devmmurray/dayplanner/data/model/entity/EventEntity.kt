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
    @ColumnInfo(name = "location")
    val location: String?,
    @ColumnInfo(name = "notes")
    val notes: String?
) {

    fun toEventObject() = Event(uid, title, dateDay, dateMonth, dateYear, location, notes)
}