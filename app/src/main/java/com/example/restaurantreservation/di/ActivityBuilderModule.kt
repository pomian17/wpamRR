package com.example.restaurantreservation.di

import com.example.restaurantreservation.ui.MainActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ActivityBuilderModule {

    @ContributesAndroidInjector(
        modules = [MainFragmentBuildersModule::class, MainViewModelsModule::class]
    )
    abstract fun contributeMainActivity(): MainActivity
}