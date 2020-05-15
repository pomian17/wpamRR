package com.example.restaurantreservation.ui.viewmodel

import android.location.Location
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.restaurantreservation.data.PlacesApi
import com.example.restaurantreservation.data.model.Restaurant
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject
import io.reactivex.rxkotlin.addTo
import timber.log.Timber

class SearchViewModel @Inject constructor(
    private val placesApi: PlacesApi
) : ViewModel() {

    private val _restaurants = MutableLiveData<List<Restaurant>>()
    val restaurants: LiveData<List<Restaurant>>
        get() = _restaurants

    private var disposables: CompositeDisposable = CompositeDisposable()

    fun searchForNearbyRestaurants(location: Location) {
        placesApi.getPlaces(location.latitude.toString()+", "+location.longitude.toString(), 2000)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                Timber.d("searchNearbyRestaurants: $it")
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