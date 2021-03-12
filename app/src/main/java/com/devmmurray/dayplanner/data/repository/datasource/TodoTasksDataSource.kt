package com.devmmurray.dayplanner.data.repository.datasource

import com.devmmurray.dayplanner.data.model.entity.TodoTaskEntity
import kotlinx.coroutines.flow.Flow

interface TodoTasksDataSource {
    suspend fun addTodoTask(task: TodoTaskEntity)
    fun getToDoTasks(): Flow<List<TodoTaskEntity>>
    suspend fun deleteToDoTask(id: Long)
}