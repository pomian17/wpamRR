package com.example.restaurantreservation.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.restaurantreservation.data.local.model.Reservation

@Database(
    entities = [Reservation::class],
    version = 3,
    exportSchema = false
)
abstract class ReservationDatabase : RoomDatabase() {

    abstract fun reservationDao(): ReservationDAO

}