package com.example.restaurantreservation.ui.viewmodel

import android.location.Location
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.restaurantreservation.data.model.Restaurant
import com.example.restaurantreservation.data.network.PlacesRepository
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import io.reactivex.schedulers.Schedulers
import timber.log.Timber
import javax.inject.Inject

class SearchViewModel @Inject constructor(
    private val placesRepository: PlacesRepository
) : ViewModel() {

    private val _restaurants = MutableLiveData<List<Restaurant>>()
    val restaurants: LiveData<List<Restaurant>>
        get() = _restaurants

    private var disposables: CompositeDisposable = CompositeDisposable()

    fun searchForNearbyRestaurants(location: Location) {
        placesRepository.getFullNearbyPlaces(
            location.latitude.toString() + ", " + location.longitude.toString(),
            2000
        )
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                Timber.d("searchNearbyRestaurants size: ${it.restaurants.size}")
                _restaurants.value = it.restaurants
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