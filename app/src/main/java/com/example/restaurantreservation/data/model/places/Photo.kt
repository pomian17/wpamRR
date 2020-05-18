package com.example.restaurantreservation.data.model.places

import com.example.restaurantreservation.BuildConfig
import com.example.restaurantreservation.util.Constants
import com.google.gson.annotations.SerializedName

data class Photo(
    var height: Int? = null,
    var width: Int? = null,
    @SerializedName("html_attributions")
    var htmlAttributions: List<String>? = null,
    @SerializedName("photo_reference")
    var photoReference: String
){

    fun getPhotoRequest():String
            = "${Constants.PLACES_BASE_URL}photo" +
                "?maxwidth=400" +
                "&maxheight=400" +
                "&photoreference=$photoReference"+
                "&key=${BuildConfig.GoogleAPIKEY}"
}