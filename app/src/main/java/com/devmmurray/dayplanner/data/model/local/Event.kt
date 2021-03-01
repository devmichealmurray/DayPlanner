package com.devmmurray.dayplanner.data.model.local

data class Event(
    val id: Long?,
    val title: String?,
    val dateDay: Int?,
    val dateMonth: Int?,
    val dateYear: Int?,
    val dateHour: Int?,
    val dateMinute: Int?,
    val timePeriod: String?,
    val locationName: String?,
    val address: String?,
    val notes: String?
)