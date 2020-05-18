package com.example.restaurantreservation.data.model.wpamrr

import android.graphics.Point

data class RestaurantObject(
    val id: Int,
    val type: Int,
    val shape: List<Point>? = null
)

object RestaurantObjectType {
    const val OTHER = 0
    const val DOOR = 1
    const val WINDOW = 2
    const val WC = 3
}