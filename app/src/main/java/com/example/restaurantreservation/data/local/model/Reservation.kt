package com.example.restaurantreservation.data.local.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "reservations")
data class Reservation(
    @PrimaryKey
    val guid: String,
    val date: Long,
    val restaurantName: String
)