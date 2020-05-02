package com.example.restaurantreservation.di

import com.example.restaurantreservation.ui.fragment.MapFragment
import com.example.restaurantreservation.ui.fragment.ResultsListFragment
import com.example.restaurantreservation.ui.fragment.SearchFragment
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

}