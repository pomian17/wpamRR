package com.example.restaurantreservation.ui.viewmodel

import androidx.lifecycle.ViewModel
import com.example.restaurantreservation.data.PlacesApi
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject
import io.reactivex.rxkotlin.addTo
import timber.log.Timber

class MapViewModel @Inject constructor(
    private val placesApi: PlacesApi
) : ViewModel() {

    private var disposables: CompositeDisposable = CompositeDisposable()
    fun initialize() {
        searchNearbyRestaurants()
    }

    private fun searchNearbyRestaurants() {
        placesApi.getPlaces("51.131406, 23.475411", 2000)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                Timber.d("searchNearbyRestaurants: $it")
            }, {
                Timber.d("searchNearbyRestaurants: error - $it")
            })
            .addTo(disposables)
    }

    override fun onCleared() {
        super.onCleared()
        disposables.clear()
    }
}