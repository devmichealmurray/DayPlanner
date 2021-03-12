package com.devmmurray.dayplanner.data.usecases.todotasks

import com.devmmurray.dayplanner.data.model.entity.TodoTaskEntity
import com.devmmurray.dayplanner.data.repository.dbrepos.TodoTasksRepository

class AddTodoTask(
    private val todoTasksRepository: TodoTasksRepository
) {
    suspend operator fun invoke(task: TodoTaskEntity) =
        todoTasksRepository.addTodoTask(task)
}

class GetToDoTasks(
    private val todoTasksRepository: TodoTasksRepository
) {
    operator fun invoke() =
        todoTasksRepository.getToDoTasks()
}

class DeleteToDoTask(
    private val todoTasksRepository: TodoTasksRepository
) {
    suspend operator fun invoke(id: Long) =
        todoTasksRepository.deleteToDoTask(id)
}