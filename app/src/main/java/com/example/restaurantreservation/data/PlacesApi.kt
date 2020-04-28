package com.example.restaurantreservation.data

import com.example.restaurantreservation.BuildConfig
import com.example.restaurantreservation.data.model.NearbySearchResponse
import io.reactivex.Flowable
import retrofit2.http.GET
import retrofit2.http.Query

interface PlacesApi {

    @GET("nearbysearch/json?key=${BuildConfig.GoogleAPIKEY}")
    fun getPlaces(
        @Query("location") location: String,
        @Query("radius") radius: Int,
        @Query("type") type: String = "restaurant"
    ):Flowable<NearbySearchResponse>
}