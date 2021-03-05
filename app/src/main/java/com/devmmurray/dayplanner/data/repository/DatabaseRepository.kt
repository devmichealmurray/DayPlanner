package com.devmmurray.dayplanner.data.repository

import com.devmmurray.dayplanner.data.database.*
import com.devmmurray.dayplanner.data.model.entity.*


class DatabaseRepository(
    private val hourlyForecastDataSource: HourlyForecastDAO,
    private val currentWeatherDataSource: CurrentWeatherDAO,
    private val todoTaskDataSource: TodoTaskDAO,
    private val eventDataSource: EventDAO,
    private val cityStateDataSource: CityStateDAO
) {

    // Hourly Weather
    suspend fun addHourlyForecasts(forecast: HourlyForecastEntity) =
        hourlyForecastDataSource.addHourlyForecasts(forecast)

    fun getHourlyForecasts() =
        hourlyForecastDataSource.getHourlyForecasts()

    suspend fun deleteOldHourlyForecasts() =
        hourlyForecastDataSource.deleteOldHourlyForecasts()

    // Current Weather
    suspend fun addCurrentWeather(weather: CurrentWeatherEntity) =
        currentWeatherDataSource.addCurrentWeather(weather)

    fun getCurrentWeather() =
        currentWeatherDataSource.getCurrentWeather()

    suspend fun deleteOldWeather() =
        currentWeatherDataSource.deleteOldWeather()


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

    suspend fun updateEvent(event: EventEntity) =
        eventDataSource.updateEvent(event)

    fun getEvents(dateId: String) =
        eventDataSource.getEvents(dateId)

    fun getEventById(id: Long) =
        eventDataSource.getEventById(id)

    suspend fun deleteEvent(id: Long) =
        eventDataSource.deleteEvent(id)

    // City State Location

    suspend fun addCityState(cityState: CityStateEntity) =
        cityStateDataSource.addCityState(cityState)

    suspend fun getCityState() =
        cityStateDataSource.getCityState()

}