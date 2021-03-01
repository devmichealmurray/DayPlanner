package com.devmmurray.dayplanner.data.repository

import com.devmmurray.dayplanner.data.database.EventDAO
import com.devmmurray.dayplanner.data.database.HourlyForecastDAO
import com.devmmurray.dayplanner.data.database.TodoTaskDAO
import com.devmmurray.dayplanner.data.model.entity.EventEntity
import com.devmmurray.dayplanner.data.model.entity.HourlyForecastEntity
import com.devmmurray.dayplanner.data.model.entity.TodoTaskEntity


class DatabaseRepository(
    private val hourlyForecastDataSource: HourlyForecastDAO,
    private val todoTaskDataSource: TodoTaskDAO,
    private val eventDataSource: EventDAO
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


    // Events
    suspend fun addEvent(event: EventEntity) =
        eventDataSource.addEvent(event)

    fun getEvents(dateId: String) =
        eventDataSource.getEvents(dateId)

    suspend fun deleteEvent(id: Long) =
        eventDataSource.deleteEvent(id)

}