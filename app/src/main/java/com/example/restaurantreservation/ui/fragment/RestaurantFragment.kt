package com.example.restaurantreservation.ui.fragment

import android.icu.text.SimpleDateFormat
import android.os.Bundle
import android.text.format.DateFormat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.restaurantreservation.R
import com.example.restaurantreservation.data.model.wpamrr.RestaurantLevel
import com.example.restaurantreservation.ui.RestaurantViewState
import com.example.restaurantreservation.ui.adapter.RestaurantAdapterModel
import com.example.restaurantreservation.ui.viewmodel.RestaurantViewModel
import com.example.restaurantreservation.ui.viewmodel.RestaurantViewModel.Companion.NEXT_TABLE
import com.example.restaurantreservation.ui.viewmodel.RestaurantViewModel.Companion.PREV_TABLE
import com.example.restaurantreservation.ui.widget.DateTimePicker
import com.example.restaurantreservation.util.Constants
import com.example.restaurantreservation.util.RestaurantMapDrawer.drawRestaurant
import com.example.restaurantreservation.viewmodel.ViewModelProviderFactory
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.fragment_restaurant.*
import kotlinx.android.synthetic.main.fragment_restaurant.restaurant_name
import kotlinx.android.synthetic.main.reservation_view_holder.*
import timber.log.Timber
import java.util.*
import javax.inject.Inject


class RestaurantFragment : DaggerFragment() {
    private lateinit var viewModel: RestaurantViewModel

    @Inject
    lateinit var providerFactory: ViewModelProviderFactory

    private var dateTimePickerDialog = DateTimePicker()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_restaurant, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        time_text.text =
            DateFormat.format(Constants.DISPLAY_DATE_FORMAT, Date(System.currentTimeMillis())).toString()
        viewModel =
            ViewModelProvider(
                requireActivity(),
                providerFactory
            ).get(RestaurantViewModel::class.java)
        val data = arguments?.getParcelable<RestaurantAdapterModel>(EXTRA_PLACE_DATA)!!
        restaurant_name.text = data.name
        viewModel.initialize(data)
        viewModel.state.observe(viewLifecycleOwner, Observer { viewState ->
            Timber.d("RestaurantViewState changed. New=$viewState")
            when (viewState) {
                is RestaurantViewState.Idle -> progress_text.isVisible = false
                is RestaurantViewState.Loading -> progress_text.isVisible = true
                is RestaurantViewState.ShowError -> {
                    Toast.makeText(context, "Error loading restaurant", Toast.LENGTH_SHORT).show()
                    findNavController().popBackStack()
                }
                is RestaurantViewState.ReservationFailed -> {
                    Toast.makeText(context, "Reservation failed!", Toast.LENGTH_SHORT).show()
                }
                is RestaurantViewState.ReservationSuccessful -> {
                    Toast.makeText(context, "Reservation successful!", Toast.LENGTH_SHORT).show()
                }
            }
        })
        viewModel.restaurantLevel.observe(
            viewLifecycleOwner,
            Observer {
                val restaurantMap = drawRestaurant(it)
                updateTableInfo(it)
                restaurantMapView.setImageBitmap(restaurantMap)
            })

        viewModel.maxLevel.observe(
            viewLifecycleOwner,
            Observer {
                if (it != null) {
                    floor_picker.minValue = 0
                    floor_picker.maxValue = it
                }
            })
        viewModel.canReserve.observe(
            viewLifecycleOwner,
            Observer {
                book_button.isEnabled = it
            })
        floor_picker.setOnValueChangedListener { _, _, newVal ->
            viewModel.selectFloor(newVal)
        }

        time_text.setOnClickListener {
            dateTimePickerDialog.dismissDialog()
            dateTimePickerDialog.showDateTimePicker(
                requireContext(),
                System.currentTimeMillis(),
                System.currentTimeMillis()
            ) { selectedTime ->
                viewModel.setDateAndRefreshTables(selectedTime)
                time_text.text =
                    DateFormat.format(Constants.DISPLAY_DATE_FORMAT, Date(selectedTime)).toString()
            }
        }

        next_table_button.setOnClickListener {
            viewModel.onTableSelected(NEXT_TABLE)
        }
        prev_table_button.setOnClickListener {
            viewModel.onTableSelected(PREV_TABLE)
        }


        email_field.doOnTextChanged { text, _, _, _ ->
            viewModel.setEmail(text.toString())
        }

        book_button.setOnClickListener {
            viewModel.sendBookingRequest()
        }

    }

    private fun updateTableInfo(data: Pair<RestaurantLevel, Int?>) {
        val tableId = data.second
        val tableCapacity = data.first.tables?.find { it.id == tableId }?.capacity ?: return
        table_info.text = getString(R.string.table_info_template).format(tableId, tableCapacity)
    }

    companion object {
        const val EXTRA_PLACE_DATA = "extra.place.data"
    }
}