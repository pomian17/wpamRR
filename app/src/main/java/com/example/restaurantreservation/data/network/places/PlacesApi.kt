package com.example.restaurantreservation.data.network.places

import com.example.restaurantreservation.BuildConfig
import com.example.restaurantreservation.data.model.places.NearbySearchResponse
import io.reactivex.Flowable
import retrofit2.http.GET
import retrofit2.http.Query

interface PlacesApi {

    @GET("nearbysearch/json?key=${BuildConfig.GoogleAPIKEY}")
    fun getPlaces(
        @Query("location") location: String,
        @Query("radius") radius: Int,
        @Query("keyword") keyword: String? = null,
        @Query("type") type: String = "restaurant"
    ): Flowable<NearbySearchResponse>

    @GET("nearbysearch/json?key=${BuildConfig.GoogleAPIKEY}")
    fun getNextPlacesPage(
        @Query("pagetoken") pageToken: String
    ): Flowable<NearbySearchResponse>
}