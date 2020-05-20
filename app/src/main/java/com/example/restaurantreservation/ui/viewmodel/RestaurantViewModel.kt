package com.example.restaurantreservation.ui.viewmodel

import android.text.format.DateFormat
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.restaurantreservation.data.local.ReservationRepository
import com.example.restaurantreservation.data.local.model.Reservation
import com.example.restaurantreservation.data.model.wpamrr.ReservationBody
import com.example.restaurantreservation.data.model.wpamrr.RestaurantDetails
import com.example.restaurantreservation.data.model.wpamrr.RestaurantLevel
import com.example.restaurantreservation.data.network.restaurantreservation.RrApi
import com.example.restaurantreservation.ui.RestaurantViewState
import com.example.restaurantreservation.ui.adapter.RestaurantAdapterModel
import com.example.restaurantreservation.util.Constants
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
    private val reservationRepository: ReservationRepository
) : ViewModel() {

    private val _state = MutableLiveData<RestaurantViewState>()
    val state: LiveData<RestaurantViewState>
        get() = _state

    private val _restaurantLevel = MutableLiveData<Pair<RestaurantLevel, Int?>>()
    val restaurantLevel: LiveData<Pair<RestaurantLevel, Int?>>
        get() = _restaurantLevel

    private val _maxLevel = MutableLiveData<Int?>()
    val maxLevel: LiveData<Int?>
        get() = _maxLevel

    private val _canReserve = MutableLiveData<Boolean>()
    val canReserve: LiveData<Boolean>
        get() = _canReserve

    private var disposables: CompositeDisposable = CompositeDisposable()

    private var restaurantDetails: RestaurantDetails? = null
    private lateinit var placeId: String
    private var email: String? = null
    private var selectedDate: Long? = null
    private lateinit var restaurantName :String

    fun initialize(data: RestaurantAdapterModel?) {
        _state.value = RestaurantViewState.Loading
        data ?: return
        this.placeId = data.placeId
        this.restaurantName = data.name
        setDateAndRefreshTables(System.currentTimeMillis())
        _canReserve.value = false
    }

    private fun getRestaurantDetails(dateTime: Long? = null) {
        val date =
            DateFormat.format(Constants.SERVER_DATE_FORMAT, Date(selectedDate?: return)).toString()
        rrApi.getRestaurant(placeId, date)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ details ->
                Timber.d("getRestaurant: $details")
                _state.value = RestaurantViewState.Idle
                details.levels?.firstOrNull()?.let { level ->
                    _restaurantLevel.value = level to level.tables?.first()?.id
                }
                _maxLevel.value = details.levels?.lastIndex
                restaurantDetails = details
                checkIfCanReserve()
            }, {
                Timber.d("getRestaurant: error - $it")
                _state.value = RestaurantViewState.ShowError
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
        checkIfCanReserve()
    }

    fun setDateAndRefreshTables(selectedTime: Long) {
        selectedDate = selectedTime
        getRestaurantDetails(selectedTime)
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
        checkIfCanReserve()
    }

    fun sendBookingRequest() {
        _canReserve.value = false
        val selectedDate = selectedDate?: return
        val date =
            DateFormat.format(Constants.SERVER_DATE_FORMAT, Date(selectedDate)).toString()
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
                _state.value = RestaurantViewState.ReservationSuccessful
                CoroutineScope(Dispatchers.IO).launch {
                    reservationRepository.insert(
                        Reservation(
                            it.guid,
                            selectedDate,
                            restaurantName
                        )
                    )
                }
                checkIfCanReserve()
            }, {
                _state.value = RestaurantViewState.ReservationFailed
                Timber.d("postReservation: error - $it")
                checkIfCanReserve()
            })
            .addTo(disposables)
    }

    fun setEmail(email: String) {
        this.email = email
        checkIfCanReserve()
    }

    private fun checkIfCanReserve() {
        var canReserve = true
        if (email?.isNotBlank() != true) canReserve = false
        val restaurantLevel = _restaurantLevel.value?.first ?: return
        val tablesList = restaurantLevel.tables ?: return
        val currentTable =
            tablesList.firstOrNull { it.id == _restaurantLevel.value?.second ?: return }
        if (currentTable?.reserved == true) canReserve = false

        _canReserve.value = canReserve
    }

    companion object {
        const val PREV_TABLE = -1
        const val NEXT_TABLE = 1
    }
}