package com.devmmurray.dayplanner.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.devmmurray.dayplanner.data.model.entity.EventEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface EventDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addEvent(event: EventEntity)

    @Query("SELECT * FROM events WHERE date_id = :dateId")
    fun getEvents(dateId: String): Flow<List<EventEntity>>

    @Query("SELECT * FROM events")
    fun getAllEvents() : Flow<List<EventEntity>>

    @Query("SELECT * FROM events WHERE uid = :id")
    fun getEventById(id: Long) : Flow<EventEntity>

    @Query("DELETE FROM events WHERE uid = :id")
    suspend fun deleteEvent(id: Long)
}