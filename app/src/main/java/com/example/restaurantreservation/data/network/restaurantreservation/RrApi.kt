package com.example.restaurantreservation.data.network.restaurantreservation

import com.example.restaurantreservation.data.model.wpamrr.PostReservationResponse
import com.example.restaurantreservation.data.model.wpamrr.ReservationBody
import com.example.restaurantreservation.data.model.wpamrr.RestaurantDetails
import com.example.restaurantreservation.data.model.wpamrr.RrRestaurantResponse
import io.reactivex.Completable
import io.reactivex.Flowable
import retrofit2.http.*

interface RrApi {

    @GET("/api/Restaurant")
    fun getRestaurants(): Flowable<RrRestaurantResponse>

    @GET("/api/Restaurant/{placeId}")
    fun getRestaurant(
        @Path("placeId") placeId: String,
        @Query("datetime") dateTime: String? = null
    ): Flowable<RestaurantDetails>

    @POST("/api/Reservation/{placeId}")
    fun postReservation(
        @Path("placeId") placeId: String,
        @Body reservationBody: ReservationBody
    ): Flowable<PostReservationResponse>

    @DELETE("/api/Reservation/{guid}")
    fun cancelReservation(
        @Path("guid") guid: String
    ): Completable
}