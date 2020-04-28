package com.example.restaurantreservation.data.model

import com.google.gson.annotations.SerializedName

data class NearbySearchResponse(
    @SerializedName("html_attributions")
    var htmlAttributions: List<Any>? = null,
    @SerializedName("next_page_token")
    var nextPageToken: String? = null,
    @SerializedName("results")
    var restaurants: List<Restaurant>? = null,
    var status: String? = null
)