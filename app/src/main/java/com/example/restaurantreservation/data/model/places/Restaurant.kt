package com.example.restaurantreservation.data.model.places

import com.example.restaurantreservation.data.model.places.Geometry
import com.example.restaurantreservation.data.model.places.OpeningHours
import com.example.restaurantreservation.data.model.places.Photo
import com.google.gson.annotations.SerializedName

data class Restaurant(
    var geometry: Geometry? = null,
    var icon: String? = null,
    var id: String? = null,
    var name: String,
    var photos: List<Photo>? = null,
    @SerializedName("place_id")
    var placeId: String,
    var reference: String? = null,
    var scope: String? = null,
    var types: List<String>? = null,
    var vicinity: String? = null,
    var rating: Double,
    @SerializedName("opening_hours")
    var openingHours: OpeningHours? = null,
    @SerializedName("price_level")
    var priceLevel: Int? = null
)