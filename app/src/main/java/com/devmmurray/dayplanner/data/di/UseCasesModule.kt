package com.devmmurray.dayplanner.data.di

import com.devmmurray.dayplanner.data.repository.dbrepos.*
import com.devmmurray.dayplanner.data.usecases.*
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
import dagger.Module
import dagger.Provides

@Module
class UseCasesModule {

    @Provides
    fun getCityUseCases(repository: CityStateRepository) =
        CityStateUseCases(
            AddCityState(repository),
            GetCityState(repository),
            DeleteCityInfo(repository)
        )

    @Provides
    fun getWeatherUseCases(repository: CurrentWeatherRepository) =
        CurrentWeatherUseCases(
            AddCurrentWeather(repository),
            GetCurrentWeather(repository),
            DeleteOldWeather(repository)
        )

    @Provides
    fun getEventUseCases(repository: EventsRepository) =
        EventUseCases(
            AddEvent(repository),
            GetEvents(repository),
            GetAllEvents(repository),
            GetEventById(repository),
            DeleteEvent(repository)
            )

    @Provides
    fun getHourlyUseCases(repository: HourlyWeatherRepository) =
        HourlyForecastsUseCases(
            AddHourlyForecasts(repository),
            GetHourlyForecasts(repository),
            DeleteOldHourlyForecasts(repository)
        )

    @Provides
    fun getNewsUseCases(repository: NewsRepository) =
        NewsUseCases(
            AddNewsArticle(repository),
            GetNewsArticle(repository),
            DeleteOldNews(repository)
        )

    @Provides
    fun getTodoTasksUseCases(repository: TodoTasksRepository) =
        TodoTasksUseCases(
            AddTodoTask(repository),
            GetToDoTasks(repository),
            DeleteToDoTask(repository)
        )

}