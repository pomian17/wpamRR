package com.example.restaurantreservation.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.restaurantreservation.data.model.wpamrr.RestaurantDetails
import com.example.restaurantreservation.data.model.wpamrr.RestaurantLevel
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

    private val _restaurantLevel = MutableLiveData<RestaurantLevel>()
    val restaurantLevel: LiveData<RestaurantLevel>
        get() = _restaurantLevel

    private val _maxLevel = MutableLiveData<Int?>()
    val maxLevel: LiveData<Int?>
        get() = _maxLevel

    private var disposables: CompositeDisposable = CompositeDisposable()

    private var restaurantDetails: RestaurantDetails? = null

    fun initialize(placeId: String?) {
        placeId ?: return
        restaurantsApi.getRestaurant(placeId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ details ->
                Timber.d("getRestaurant: $details")
                details.levels?.firstOrNull()?.let { _restaurantLevel.value = it }
                _maxLevel.value = details.levels?.lastIndex
                restaurantDetails = details
            }, {
                Timber.d("getRestaurant: error - $it")
            })
            .addTo(disposables)

    }

    override fun onCleared() {
        super.onCleared()
        disposables.clear()
    }

    fun selectFloor(floorNr: Int) {
        restaurantDetails?.levels?.getOrNull(floorNr)?.let {
            _restaurantLevel.value = it
        }
    }
}