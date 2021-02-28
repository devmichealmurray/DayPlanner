package com.devmmurray.dayplanner.data.repository

import com.devmmurray.dayplanner.data.database.HourlyForecastDAO
import com.devmmurray.dayplanner.data.database.TodoTaskDAO
import com.devmmurray.dayplanner.data.model.entity.HourlyForecastEntity
import com.devmmurray.dayplanner.data.model.entity.TodoTaskEntity


class DatabaseRepository(
    private val hourlyForecastDataSource: HourlyForecastDAO,
    private val todoTaskDataSource: TodoTaskDAO
) {

    // Weather
    suspend fun addHourlyForecasts(forecast: HourlyForecastEntity) =
        hourlyForecastDataSource.addHourlyForecasts(forecast)

    fun getHourlyForecasts() =
        hourlyForecastDataSource.getHourlyForecasts()

    suspend fun deleteOldHourlyForecasts() =
        hourlyForecastDataSource.deleteOldHourlyForecasts()


    // To Do Tasks
    suspend fun addTodoTask(task: TodoTaskEntity) =
        todoTaskDataSource.addTodoTask(task)

    fun getToDoTasks() =
        todoTaskDataSource.getTodoTasks()

    suspend fun deleteToDoTask(id: Long) =
        todoTaskDataSource.deleteTodoTask(id)

}