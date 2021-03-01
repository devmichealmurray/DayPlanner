package com.devmmurray.dayplanner.data.database

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

    @Query("DELETE FROM events WHERE uid = :id")
    suspend fun deleteEvent(id: Long)
}