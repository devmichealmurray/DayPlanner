package com.devmmurray.dayplanner.data.model.local

data class Event(
    val id: Long?,
    val title: String?,
    val dateDay: Int?,
    val dateMonth: Int?,
    val dateYear: Int?,
    val location: String?,
    val notes: String?
)