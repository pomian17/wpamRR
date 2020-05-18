package com.example.restaurantreservation.data.model.wpamrr

import android.graphics.Point

data class RestaurantObject(
    val id: Int,
    val type: RestaurantObjectType,
    val shape: List<Point>? = null
)

