package com.devmmurray.dayplanner.data.repository.datasource

import com.devmmurray.dayplanner.data.model.entity.EventEntity
import kotlinx.coroutines.flow.Flow

interface EventsDataSource {
    suspend fun addEvent(event: EventEntity)
    fun getEvents(dateId: String): Flow<List<EventEntity>>
    fun getAllEvents(): Flow<List<EventEntity>>
    fun getEventById(id: Long): Flow<EventEntity>
    suspend fun deleteEvent(id: Long)
}