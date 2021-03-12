package com.devmmurray.dayplanner.data.repository.dbrepos

import com.devmmurray.dayplanner.data.model.entity.EventEntity
import com.devmmurray.dayplanner.data.repository.datasource.EventsDataSource

class EventsRepository(
    private val eventDataSource: EventsDataSource
) {
    suspend fun addEvent(event: EventEntity) =
        eventDataSource.addEvent(event)

    fun getEvents(dateId: String) =
        eventDataSource.getEvents(dateId)

    fun getAllEvents() =
        eventDataSource.getAllEvents()

    fun getEventById(id: Long) =
        eventDataSource.getEventById(id)

    suspend fun deleteEvent(id: Long) =
        eventDataSource.deleteEvent(id)
}