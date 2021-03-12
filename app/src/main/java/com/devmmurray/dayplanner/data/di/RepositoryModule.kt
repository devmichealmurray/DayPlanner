package com.devmmurray.dayplanner.data.di

import android.app.Application
import com.devmmurray.dayplanner.data.database.roomdatasource.*
import com.devmmurray.dayplanner.data.repository.dbrepos.*
import dagger.Module
import dagger.Provides

@Module
class RepositoryModule {

    @Provides
    fun providesCityRepository(app: Application) =
        CityStateRepository(RoomCityStateDataSource(app))

    @Provides
    fun providesWeatherRepository(app: Application) =
        CurrentWeatherRepository(RoomWeatherDataSource(app))

    @Provides
    fun providesEventsRepository(app: Application) =
        EventsRepository(RoomEventsDataSource(app))

    @Provides
    fun providesHourlyRepository(app: Application) =
        HourlyWeatherRepository(RoomHourlyDataSource(app))

    @Provides
    fun providesNewsRepository(app: Application) =
        NewsRepository(RoomNewDataSource(app))

    @Provides
    fun providesTodoRepository(app: Application) =
        TodoTasksRepository(RoomTodoDataSource(app))
}