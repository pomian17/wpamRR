package com.example.restaurantreservation.di

import android.content.Context
import com.example.restaurantreservation.RestaurantReservationApp
import com.example.restaurantreservation.data.network.places.PlacesApi
import com.example.restaurantreservation.data.network.restaurantreservation.RrApi
import com.example.restaurantreservation.util.Constants
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Named
import javax.inject.Singleton


@Module
class AppModule {

    @Singleton
    @Provides
    fun provideContext(app: RestaurantReservationApp): Context = app.applicationContext

    @Singleton
    @Provides
    @Named("GooglePlacesRetrofitInstance")
    fun provideGooglePlacesRetrofitInstance(): Retrofit =
        Retrofit.Builder()
            .baseUrl(Constants.PLACES_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()

    @Singleton
    @Provides
    @Named("RestaurantReservationRetrofitInstance")
    fun provideRestaurantReservationRetrofitInstance(): Retrofit =
        Retrofit.Builder()
            .baseUrl(Constants.RR_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()

    @Provides
    fun providePlacesApi(
        @Named("GooglePlacesRetrofitInstance") retrofit: Retrofit
    ): PlacesApi {
        return retrofit.create(PlacesApi::class.java)
    }

    @Provides
    fun provideRestaurantsApi(
        @Named("RestaurantReservationRetrofitInstance") retrofit: Retrofit
    ): RrApi {
        return retrofit.create(RrApi::class.java)
    }
}
