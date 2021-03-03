package com.devmmurray.dayplanner.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.devmmurray.dayplanner.data.model.entity.TodoTaskEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TodoTaskDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addTodoTask(task: TodoTaskEntity)

    @Query("SELECT * FROM todo_tasks")
    fun getTodoTasks(): Flow<List<TodoTaskEntity>>

    @Query("DELETE FROM todo_tasks WHERE uid = :id")
    suspend fun deleteTodoTask(id: Long)
}