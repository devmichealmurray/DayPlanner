package com.devmmurray.dayplanner.data.database.roomdatasource

import android.content.Context
import com.devmmurray.dayplanner.data.database.RoomDatabaseClient
import com.devmmurray.dayplanner.data.model.entity.EventEntity
import com.devmmurray.dayplanner.data.repository.datasource.EventsDataSource
import kotlinx.coroutines.flow.Flow

class RoomEventsDataSource(context: Context) : EventsDataSource {
    private val eventDao = RoomDatabaseClient.getDbInstance(context).eventDAO()

    override suspend fun addEvent(event: EventEntity) =
        eventDao.addEvent(event)

    override fun getEvents(dateId: String) : Flow<List<EventEntity>> =
        eventDao.getEvents(dateId)

    override fun getAllEvents() : Flow<List<EventEntity>> =
        eventDao.getAllEvents()

    override fun getEventById(id: Long) : Flow<EventEntity> =
        eventDao.getEventById(id)

    override suspend fun deleteEvent(id: Long) =
        eventDao.deleteEvent(id)
}