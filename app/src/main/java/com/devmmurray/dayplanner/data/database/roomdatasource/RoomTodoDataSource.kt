package com.devmmurray.dayplanner.data.database.roomdatasource

import android.content.Context
import com.devmmurray.dayplanner.data.database.RoomDatabaseClient
import com.devmmurray.dayplanner.data.model.entity.TodoTaskEntity
import com.devmmurray.dayplanner.data.repository.datasource.TodoTasksDataSource
import kotlinx.coroutines.flow.Flow

class RoomTodoDataSource(context: Context) : TodoTasksDataSource {
    private val todoDao = RoomDatabaseClient.getDbInstance(context).todoTaskDAO()

    override suspend fun addTodoTask(task: TodoTaskEntity) =
        todoDao.addTodoTask(task)

    override fun getToDoTasks(): Flow<List<TodoTaskEntity>> = todoDao.getTodoTasks()

    override suspend fun deleteToDoTask(id: Long) = todoDao.deleteTodoTask(id)
}