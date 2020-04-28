package com.example.restaurantreservation.data.model

import com.google.gson.annotations.SerializedName

data class Location(
    @SerializedName("lat")
    var latitude: Double,
    @SerializedName("lng")
    var longitude: Double
)