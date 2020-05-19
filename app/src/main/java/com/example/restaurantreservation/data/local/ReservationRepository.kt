package com.example.restaurantreservation.data.local

import com.example.restaurantreservation.data.local.model.Reservation
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ReservationRepository @Inject constructor(private val reservationDAO: ReservationDAO) {

    suspend fun insert(reservation: Reservation) {
        reservationDAO.insert(reservation)
    }

    suspend fun delete(guid: String) {
        reservationDAO.deleteReservation(guid)
    }

    fun getAllReservations() = reservationDAO.getAllReservations()

}