package com.example.restaurantreservation.data.model.places

import com.example.restaurantreservation.data.model.places.Location

data class Viewport(
    var northeast: Location? = null,
    var southwest: Location? = null
)