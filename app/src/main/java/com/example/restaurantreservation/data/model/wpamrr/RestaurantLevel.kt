package com.example.restaurantreservation.data.model.wpamrr

import android.graphics.Point

data class RestaurantLevel (
    val levelNo: Int,
    val shape: List<Point>? = null,
    val tables: List<RestaurantTable>? = null,
    val objects: List<RestaurantObject>? = null
)