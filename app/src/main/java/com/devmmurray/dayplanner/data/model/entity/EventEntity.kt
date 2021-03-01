package com.devmmurray.dayplanner.data.model.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.devmmurray.dayplanner.data.model.local.Event

@Entity(tableName = "events")
class EventEntity(
    @PrimaryKey(autoGenerate = true)
    val uid: Long = 0L,
    @ColumnInfo(name = "date_id")
    val dateId: String?,
    @ColumnInfo(name = "title")
    val title: String?,
    @ColumnInfo(name = "event_time")
    val eventTime: Long?,
    @ColumnInfo(name = "location")
    val locationName: String?,
    @ColumnInfo(name = "address")
    val address: String?,
    @ColumnInfo(name = "notes")
    val notes: String?
) {

    fun toEventObject() = Event(
        uid,
        dateId,
        title,
        eventTime,
        locationName,
        address,
        notes
    )
}