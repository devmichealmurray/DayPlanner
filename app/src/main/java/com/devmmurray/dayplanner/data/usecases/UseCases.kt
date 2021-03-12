package com.devmmurray.dayplanner.data.usecases

import com.devmmurray.dayplanner.data.usecases.citystate.AddCityState
import com.devmmurray.dayplanner.data.usecases.citystate.DeleteCityInfo
import com.devmmurray.dayplanner.data.usecases.citystate.GetCityState
import com.devmmurray.dayplanner.data.usecases.currentweather.AddCurrentWeather
import com.devmmurray.dayplanner.data.usecases.currentweather.DeleteOldWeather
import com.devmmurray.dayplanner.data.usecases.currentweather.GetCurrentWeather
import com.devmmurray.dayplanner.data.usecases.events.*
import com.devmmurray.dayplanner.data.usecases.hourlyforecasts.AddHourlyForecasts
import com.devmmurray.dayplanner.data.usecases.hourlyforecasts.DeleteOldHourlyForecasts
import com.devmmurray.dayplanner.data.usecases.hourlyforecasts.GetHourlyForecasts
import com.devmmurray.dayplanner.data.usecases.news.AddNewsArticle
import com.devmmurray.dayplanner.data.usecases.news.DeleteOldNews
import com.devmmurray.dayplanner.data.usecases.news.GetNewsArticle
import com.devmmurray.dayplanner.data.usecases.todotasks.AddTodoTask
import com.devmmurray.dayplanner.data.usecases.todotasks.DeleteToDoTask
import com.devmmurray.dayplanner.data.usecases.todotasks.GetToDoTasks

data class CityStateUseCases(
    val addCityState: AddCityState,
    val getCityState: GetCityState,
    val deleteCityInfo: DeleteCityInfo
)

data class CurrentWeatherUseCases(
    val addCurrentWeather: AddCurrentWeather,
    val getCurrentWeather: GetCurrentWeather,
    val deleteOldWeather: DeleteOldWeather,
)

data class EventUseCases(
    val addEvent: AddEvent,
    val getEvents: GetEvents,
    val getAllEvents: GetAllEvents,
    val getEventById: GetEventById,
    val deleteEvent: DeleteEvent
)

data class HourlyForecastsUseCases(
    val addHourlyForecasts: AddHourlyForecasts,
    val getHourlyForecasts: GetHourlyForecasts,
    val deleteOldHourlyForecasts: DeleteOldHourlyForecasts,
)

data class NewsUseCases(
    val addNewsArticle: AddNewsArticle,
    val getNewsArticle: GetNewsArticle,
    val deleteOldNews: DeleteOldNews
)

data class TodoTasksUseCases(
    val addTodoTask: AddTodoTask,
    val getToDoTasks: GetToDoTasks,
    val deleteToDoTask: DeleteToDoTask
)