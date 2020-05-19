package com.example.restaurantreservation.di

import com.example.restaurantreservation.RestaurantReservationApp
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjectionModule
import dagger.android.AndroidInjector
import javax.inject.Singleton


@Singleton
@Component(
    modules = [
        AndroidInjectionModule::class,
        ActivityBuilderModule::class,
        AppModule::class,
        ViewModelFactoryModule::class,
        RoomModule::class
    ]
)
interface AppComponent : AndroidInjector<RestaurantReservationApp> {

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun application(application: RestaurantReservationApp): Builder

        fun build(): AppComponent
    }
}