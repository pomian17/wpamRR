package com.example.restaurantreservation.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.restaurantreservation.data.model.places.Restaurant
import com.example.restaurantreservation.data.network.PlacesRepository
import com.example.restaurantreservation.ui.fragment.SearchFragment
import com.google.android.gms.maps.model.LatLngBounds
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

    private val _status = MutableLiveData<SearchFragment.Companion.LoadingStatus>()
    val status: LiveData<SearchFragment.Companion.LoadingStatus>
        get() = _status

    private var disposables: CompositeDisposable = CompositeDisposable()

    private var searchArea: LatLngBounds? = null

    fun searchForNearbyRestaurants(keyword: String?) {
        _status.value = SearchFragment.Companion.LoadingStatus.LOADING
        searchArea?.let {
            placesRepository.getFullNearbyPlaces(it, keyword)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ restaurants ->
                    Timber.d("searchNearbyRestaurants size: ${restaurants.size}")
                    _restaurants.value = restaurants
                    _status.value = SearchFragment.Companion.LoadingStatus.LOADED
                }, {
                    Timber.d("searchNearbyRestaurants: error - $it")
                    _status.value = SearchFragment.Companion.LoadingStatus.ERROR
                })
                .addTo(disposables)
        }
    }

    override fun onCleared() {
        super.onCleared()
        disposables.clear()
    }

    fun setSearchArea(latLngBounds: LatLngBounds?) {
        searchArea = latLngBounds
    }
}