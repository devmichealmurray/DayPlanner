package com.devmmurray.dayplanner.data.model.local

data class Event(
    val id: Long?,
    val dateId: String?,
    val title: String?,
    val eventTime: String?,
    val locationName: String?,
    val address: String?,
    val notes: String?
)