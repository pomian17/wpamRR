package com.example.restaurantreservation.data.local

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.restaurantreservation.data.local.model.Reservation


@Dao
interface ReservationDAO {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(entry: Reservation)

    @Query("SELECT * from reservations")
    fun getAllReservations(): LiveData<List<Reservation>>

    @Query("DELETE FROM reservations WHERE guid = :guid")
    suspend fun deleteReservation(guid:String)
}