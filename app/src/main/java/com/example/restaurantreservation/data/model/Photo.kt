package com.example.restaurantreservation.data.model

import com.google.gson.annotations.SerializedName

data class Photo(
    var height: Int? = null,
    var width: Int? = null,
    @SerializedName("html_attributions")
    var htmlAttributions: List<String>? = null,
    @SerializedName("photo_reference")
    var photoReference: String? = null
)