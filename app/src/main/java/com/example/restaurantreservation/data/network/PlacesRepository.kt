package com.example.restaurantreservation.data.network

import android.location.Location
import com.example.restaurantreservation.data.model.NearbySearchResponse
import com.example.restaurantreservation.data.model.Restaurant
import com.example.restaurantreservation.data.model.RrRestaurant
import com.example.restaurantreservation.data.model.RrRestaurantResponse
import com.example.restaurantreservation.data.network.places.PlacesApi
import com.example.restaurantreservation.data.network.restaurantreservation.RestaurantsApi
import com.google.android.gms.maps.model.LatLngBounds
import com.google.gson.Gson
import io.reactivex.Flowable
import io.reactivex.functions.BiFunction
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class PlacesRepository @Inject constructor(
    private val placesApi: PlacesApi,
    private val restaurantsApi: RestaurantsApi
) {

    fun getFullNearbyPlaces(
        bounds: LatLngBounds
    ): Flowable<List<Restaurant>> {
        return Flowable.combineLatest<List<Restaurant>, RrRestaurantResponse, List<Restaurant>>(
            getGoogleNearbyPlaces(bounds),
            restaurantsApi.getRestaurants(),
            BiFunction { nearbySearchResponse, rrRestaurantResponse ->
                nearbySearchResponse.forEach {
                    it.isInRrDatabase =
                        rrRestaurantResponse.restaurants.contains(RrRestaurant(it.placeId))
                }
                nearbySearchResponse
            }
        )
    }

    fun getRestaurants(): Flowable<RrRestaurantResponse> =
        restaurantsApi.getRestaurants()

    private fun getGoogleNearbyPlaces(
        bounds: LatLngBounds,
        type: String = "restaurant"
    ): Flowable<List<Restaurant>> {
        val location = bounds.center
        val distanceResults = FloatArray(3)
        Location.distanceBetween(
            bounds.northeast.latitude,
            bounds.northeast.longitude,
            bounds.southwest.latitude,
            bounds.southwest.longitude,
            distanceResults
        )
        return placesApi.getPlaces(
            location.latitude.toString() + ", " + location.longitude.toString(),
            distanceResults.first().toInt(),
            type
        )
            .flatMap { getNextPages(it.nextPageToken, it.restaurants.orEmpty()) }
    }

    private fun getNextPages(
        nextPageToken: String?,
        restaurants: List<Restaurant>
    ): Flowable<List<Restaurant>> =
        if (nextPageToken != null) {
            getNextPlacesPage(nextPageToken)
                .flatMap {
                    getNextPages(it.nextPageToken, restaurants + it.restaurants.orEmpty())
                }
        } else {
            Flowable.just(restaurants)
        }


    private fun getNextPlacesPage(
        nextPageToken: String,
        tryNumber: Int = 1
    ): Flowable<NearbySearchResponse> =
        Flowable.timer(RETRY_DELAY, TimeUnit.MILLISECONDS).flatMap {
            placesApi.getNextPlacesPage(nextPageToken)
                .flatMap { response ->
                    if (response.status == TOO_EARLY_ERROR_MESSAGE && tryNumber < MAX_RETRIES) {
                        getNextPlacesPage(nextPageToken, tryNumber + 1)
                    } else {
                        Flowable.just(response)
                    }
                }
        }


    companion object {
        const val TOO_EARLY_ERROR_MESSAGE = "INVALID_REQUEST"
        const val MAX_RETRIES = 2
        const val RETRY_DELAY = 2500L
    }
}