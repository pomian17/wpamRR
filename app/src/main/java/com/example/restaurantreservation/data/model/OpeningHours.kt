package com.example.restaurantreservation.data.model

import com.google.gson.annotations.SerializedName

data class OpeningHours(
    @SerializedName("open_now")
    var openNow: Boolean? = null,
    @SerializedName("weekday_text")
    var weekdayText: List<Any>? = null
)