package com.devmmurray.dayplanner.data.repository

import com.devmmurray.dayplanner.data.database.dao.*
import com.devmmurray.dayplanner.data.model.entity.*


class DatabaseRepository(
    private val hourlyForecastDataSource: HourlyForecastDAO,
    private val currentWeatherDataSource: CurrentWeatherDAO,
    private val todoTaskDataSource: TodoTaskDAO,
    private val eventDataSource: EventDAO,
    private val cityStateDataSource: CityStateDAO,
    private val newsDataSource: NewsDAO
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

    fun getEvents(dateId: String) =
        eventDataSource.getEvents(dateId)

    fun getAllEvents() =
        eventDataSource.getAllEvents()

    fun getEventById(id: Long) =
        eventDataSource.getEventById(id)

    suspend fun deleteEvent(id: Long) =
        eventDataSource.deleteEvent(id)

    // News

    suspend fun addNewsArticle(article: NewsEntity) =
        newsDataSource.addNewsArticle(article)

    fun getNewsArticles() =
        newsDataSource.getNewsArticles()

    suspend fun deleteOldNews() =
        newsDataSource.deleteOldNewsArticles()

    // City State Location

    suspend fun addCityState(cityState: CityStateEntity) =
        cityStateDataSource.addCityState(cityState)

    suspend fun getCityState() =
        cityStateDataSource.getCityState()

    suspend fun deleteCityInfo() =
        cityStateDataSource.deleteCityInfo()

}