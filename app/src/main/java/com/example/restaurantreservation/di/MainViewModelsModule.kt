package com.example.restaurantreservation.di

import androidx.lifecycle.ViewModel
import com.example.restaurantreservation.ui.viewmodel.ReservationsViewModel
import com.example.restaurantreservation.ui.viewmodel.RestaurantViewModel
import com.example.restaurantreservation.ui.viewmodel.SearchViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class MainViewModelsModule {

    @Binds
    @IntoMap
    @ViewModelKey(SearchViewModel::class)
    abstract fun bindMapViewModel(viewModel: SearchViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(RestaurantViewModel::class)
    abstract fun bindRestaurantViewModel(viewModel: RestaurantViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(ReservationsViewModel::class)
    abstract fun bindReservationsViewModel(viewModel: ReservationsViewModel): ViewModel
}