package com.example.restaurantreservation.data.network.restaurantreservation

import com.example.restaurantreservation.data.model.RrRestaurantResponse
import io.reactivex.Flowable
import retrofit2.http.GET

interface RestaurantsApi {

    @GET("/api/Restaurant")
    fun getRestaurants(): Flowable<RrRestaurantResponse>
}