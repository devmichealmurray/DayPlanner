package com.devmmurray.dayplanner.data.repository.dbrepos

import com.devmmurray.dayplanner.data.model.entity.TodoTaskEntity
import com.devmmurray.dayplanner.data.repository.datasource.TodoTasksDataSource

class TodoTasksRepository(
    private val todoTaskDataSource: TodoTasksDataSource
) {
    suspend fun addTodoTask(task: TodoTaskEntity) =
        todoTaskDataSource.addTodoTask(task)

    fun getToDoTasks() =
        todoTaskDataSource.getToDoTasks()

    suspend fun deleteToDoTask(id: Long) =
        todoTaskDataSource.deleteToDoTask(id)
}