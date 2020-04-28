package com.example.restaurantreservation.data.model

import com.google.gson.annotations.SerializedName

data class Restaurant(
    var geometry: Geometry? = null,
    var icon: String? = null,
    var id: String? = null,
    var name: String? = null,
    var photos: List<Photo>? = null,
    @SerializedName("place_id")
    var placeId: String? = null,
    var reference: String? = null,
    var scope: String? = null,
    var types: List<String>? = null,
    var vicinity: String? = null,
    var rating: Double? = null,
    @SerializedName("opening_hours")
    var openingHours: OpeningHours? = null,
    @SerializedName("price_level")
    var priceLevel: Int? = null
)