package com.devmmurray.dayplanner.data.di

import com.devmmurray.dayplanner.ui.viewmodel.SplashActivityViewModel
import dagger.Component

@Component(
    modules = [
        ApplicationModule::class,
        RepositoryModule::class,
        UseCasesModule::class
    ]
)
interface ViewModelComponent {

    fun inject(splashActivityViewModel: SplashActivityViewModel)
}