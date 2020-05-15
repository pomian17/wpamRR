package com.example.restaurantreservation

import com.example.restaurantreservation.di.DaggerAppComponent
import com.microsoft.appcenter.AppCenter
import com.microsoft.appcenter.analytics.Analytics
import com.microsoft.appcenter.crashes.Crashes
import dagger.android.AndroidInjector
import dagger.android.DaggerApplication
import timber.log.Timber

class RestaurantReservationApp : DaggerApplication() {

    override fun applicationInjector(): AndroidInjector<out DaggerApplication> =
        DaggerAppComponent.builder().application(this).build()

    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
        AppCenter.start(
            this, "765e4937-01fb-4790-bae1-3cbc677372b2",
            Analytics::class.java, Crashes::class.java
        )
    }
}