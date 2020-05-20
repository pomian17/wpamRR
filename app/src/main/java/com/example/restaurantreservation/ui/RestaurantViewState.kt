package com.example.restaurantreservation.ui

sealed class RestaurantViewState {
    object Idle : RestaurantViewState()
    object Loading : RestaurantViewState()
    object ShowError : RestaurantViewState()
    object ReservationFailed: RestaurantViewState()
    object ReservationSuccessful : RestaurantViewState()
}