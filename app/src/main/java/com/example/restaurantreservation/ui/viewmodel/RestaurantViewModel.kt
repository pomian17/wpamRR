package com.example.restaurantreservation.ui.viewmodel

import android.text.format.DateFormat
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.restaurantreservation.data.model.wpamrr.ReservationBody
import com.example.restaurantreservation.data.model.wpamrr.RestaurantDetails
import com.example.restaurantreservation.data.model.wpamrr.RestaurantLevel
import com.example.restaurantreservation.data.network.restaurantreservation.RrApi
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import io.reactivex.schedulers.Schedulers
import timber.log.Timber
import java.util.*
import javax.inject.Inject


class RestaurantViewModel @Inject constructor(
    private val rrApi: RrApi
) : ViewModel() {

    private val _restaurantLevel = MutableLiveData<Pair<RestaurantLevel, Int?>>()
    val restaurantLevel: LiveData<Pair<RestaurantLevel, Int?>>
        get() = _restaurantLevel

    private val _maxLevel = MutableLiveData<Int?>()
    val maxLevel: LiveData<Int?>
        get() = _maxLevel

    private val _selectedDate = MutableLiveData<String>()
    val selectedDate: LiveData<String>
        get() = _selectedDate
    private var disposables: CompositeDisposable = CompositeDisposable()

    private val _tables = MutableLiveData<List<Int>>()
    val tables: LiveData<List<Int>>
        get() = _tables

    private var restaurantDetails: RestaurantDetails? = null
    private lateinit var placeId: String
    private var email: String? = null

    fun initialize(placeId: String?) {
        placeId ?: return
        this.placeId = placeId
        setDateAndRefreshTables(System.currentTimeMillis())
    }

    private fun getRestaurantDetails(dateTime: String? = null) {
        rrApi.getRestaurant(placeId, dateTime)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ details ->
                Timber.d("getRestaurant: $details")
                details.levels?.firstOrNull()?.let { level ->
                    _restaurantLevel.value = level to null
                    _tables.value = level.tables?.map { it.id }
                }
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
        restaurantDetails?.levels?.getOrNull(floorNr)?.let { level ->
            _restaurantLevel.value = level to null
            _tables.value = level.tables?.map { it.id }
        }
    }

    fun setDateAndRefreshTables(selectedTime: Long) {
        val dateString = DateFormat.format("yyyy-MM-ddTHH:mm:ssZ", Date(selectedTime)).toString()
        _selectedDate.value = dateString
        getRestaurantDetails(dateString)
    }

    fun onTableSelected(selectedTable: Int?) {
        _restaurantLevel.value?.first?.let { currentRestauratLevel ->
            _restaurantLevel.value = currentRestauratLevel to selectedTable
        }
    }

    fun sendBookingRequest() {
        val date = _selectedDate.value ?: return
        val selectedTableId = _restaurantLevel.value?.second ?: return
        val email = this.email ?: return
        rrApi.postReservation(
            placeId,
            ReservationBody(
                date,
                selectedTableId,
                email
            )
        ).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                Timber.d("postReservation: $it")
            }, {
                Timber.d("postReservation: error - $it")
            })
            .addTo(disposables)
    }

    fun setEmail(email: String) {
        this.email = email
    }
}