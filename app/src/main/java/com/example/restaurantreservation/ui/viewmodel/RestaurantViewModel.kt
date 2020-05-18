package com.example.restaurantreservation.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.restaurantreservation.data.model.wpamrr.RestaurantDetails
import com.example.restaurantreservation.data.network.restaurantreservation.RestaurantsApi
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import io.reactivex.schedulers.Schedulers
import timber.log.Timber
import javax.inject.Inject

class RestaurantViewModel @Inject constructor(
    private val restaurantsApi: RestaurantsApi
) : ViewModel() {

    private val _restaurantData = MutableLiveData<RestaurantDetails>()
    val restaurantData: LiveData<RestaurantDetails>
        get() = _restaurantData

    private var disposables: CompositeDisposable = CompositeDisposable()

    fun initialize(placeId: String?) {
        placeId?.let {
            restaurantsApi.getRestaurant(it)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    Timber.d("getRestaurant: $it")
                    _restaurantData.value = it
                }, {
                    Timber.d("getRestaurant: error - $it")
                })
                .addTo(disposables)
        }
    }

    override fun onCleared() {
        super.onCleared()
        disposables.clear()
    }
}