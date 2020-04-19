package com.example.restaurantreservation.di

import com.example.restaurantreservation.ui.fragment.MapFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class MainFragmentBuildersModule {
    @ContributesAndroidInjector
    abstract fun contributeMapFragment(): MapFragment
}