package com.example.restaurantreservation.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.restaurantreservation.data.local.ReservationRepository
import com.example.restaurantreservation.data.local.model.Reservation
import com.example.restaurantreservation.data.network.restaurantreservation.RrApi
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

class ReservationsViewModel @Inject constructor(
    private val rrApi: RrApi,
    private val reservationRepository: ReservationRepository
) : ViewModel() {

    private var disposables: CompositeDisposable = CompositeDisposable()

    var reservations: LiveData<List<Reservation>> = reservationRepository.getAllReservations()


    fun cancelReservation(guid: String) {
        rrApi.cancelReservation(guid)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                Timber.d("getRestaurant: success")
                CoroutineScope(Dispatchers.IO).launch {
                    reservationRepository.delete(guid)
                }
            }, {
                Timber.d("getRestaurant: error - $it")
            })
            .addTo(disposables)
    }

}