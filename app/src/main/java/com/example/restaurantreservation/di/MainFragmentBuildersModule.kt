package com.example.restaurantreservation.di

import com.example.restaurantreservation.ui.fragment.*
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class MainFragmentBuildersModule {

    @ContributesAndroidInjector
    abstract fun contributeSearchFragment(): SearchFragment

    @ContributesAndroidInjector
    abstract fun contributeMapFragment(): MapFragment

    @ContributesAndroidInjector
    abstract fun contributeResultsListFragment(): ResultsListFragment

    @ContributesAndroidInjector
    abstract fun contributeRestaurantFragment(): RestaurantFragment

    @ContributesAndroidInjector
    abstract fun contributeReservationsFragment(): ReservationsFragment

}