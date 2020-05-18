package com.example.restaurantreservation.data.network.restaurantreservation

import com.example.restaurantreservation.data.model.wpamrr.RestaurantDetails
import com.example.restaurantreservation.data.model.wpamrr.RrRestaurantResponse
import io.reactivex.Flowable
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface RestaurantsApi {

    @GET("/api/Restaurant")
    fun getRestaurants(): Flowable<RrRestaurantResponse>

    @GET("/api/Restaurant/{placeId}")
    fun getRestaurant(
        @Path("placeId") placeId: String,
        @Query("datetime") dateTime: String? = null
    ): Flowable<RestaurantDetails>
}