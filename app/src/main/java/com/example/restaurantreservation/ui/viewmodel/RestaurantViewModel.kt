package com.example.restaurantreservation.ui.viewmodel

import android.content.Context
import android.text.format.DateFormat
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.restaurantreservation.data.local.ReservationRepository
import com.example.restaurantreservation.data.local.model.Reservation
import com.example.restaurantreservation.data.model.wpamrr.ReservationBody
import com.example.restaurantreservation.data.model.wpamrr.RestaurantDetails
import com.example.restaurantreservation.data.model.wpamrr.RestaurantLevel
import com.example.restaurantreservation.data.network.restaurantreservation.RrApi
import com.example.restaurantreservation.ui.adapter.RestaurantAdapterModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import timber.log.Timber
import java.util.*
import javax.inject.Inject


class RestaurantViewModel @Inject constructor(
    private val rrApi: RrApi,
    private val reservationRepository: ReservationRepository,
    private val context: Context //TODO only temporary
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

    private var restaurantDetails: RestaurantDetails? = null
    private lateinit var placeId: String
    private var email: String? = null

    fun initialize(data: RestaurantAdapterModel?) {
        data ?: return
        this.placeId = data.placeId
        setDateAndRefreshTables(System.currentTimeMillis())
    }

    private fun getRestaurantDetails(dateTime: String? = null) {
        rrApi.getRestaurant(placeId, dateTime)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ details ->
                Timber.d("getRestaurant: $details")
                details.levels?.firstOrNull()?.let { level ->
                    _restaurantLevel.value = level to level.tables?.first()?.id
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
            _restaurantLevel.value = level to level.tables?.first()?.id
        }
    }

    fun setDateAndRefreshTables(selectedTime: Long) {
        val dateString = DateFormat.format("yyyy-MM-ddTHH:mm:ssZ", Date(selectedTime)).toString()
        _selectedDate.value = dateString
        getRestaurantDetails(dateString)
    }

    fun onTableSelected(buttonAction: Int) {
        val restaurantLevel = _restaurantLevel.value?.first ?: return
        val tablesList = restaurantLevel.tables ?: return
        val currentTablePosition =
            tablesList.indexOfFirst { it.id == _restaurantLevel.value?.second ?: return }

        var newTablePosition = currentTablePosition + buttonAction
        if (newTablePosition < 0) newTablePosition = tablesList.lastIndex
        if (newTablePosition > tablesList.lastIndex) newTablePosition = 0
        val newTableId = tablesList[newTablePosition].id

        _restaurantLevel.value = restaurantLevel to newTableId

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
                Toast.makeText(context, "Reservation successful!", Toast.LENGTH_SHORT).show()
                CoroutineScope(Dispatchers.IO).launch {
                    reservationRepository.insert(
                        Reservation(
                            it.guid,
                            date,
                            placeId
                        )
                    )
                }
            }, {
                Toast.makeText(context, "Reservation failed!", Toast.LENGTH_SHORT).show()
                Timber.d("postReservation: error - $it")
            })
            .addTo(disposables)
    }

    fun setEmail(email: String) {
        this.email = email
    }

    companion object {
        const val PREV_TABLE = -1
        const val NEXT_TABLE = 1
    }
}