package com.example.restaurantreservation.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.restaurantreservation.R
import com.example.restaurantreservation.ui.viewmodel.RestaurantViewModel
import com.example.restaurantreservation.ui.widget.DateTimePicker
import com.example.restaurantreservation.util.RestaurantMapDrawer.drawRestaurant
import com.example.restaurantreservation.viewmodel.ViewModelProviderFactory
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.fragment_restaurant.*
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
        viewModel =
            ViewModelProvider(
                requireActivity(),
                providerFactory
            ).get(RestaurantViewModel::class.java)
        viewModel.initialize(arguments?.getString(EXTRA_PLACE_ID))
        viewModel.restaurantLevel.observe(
            viewLifecycleOwner,
            Observer {
                val restaurantMap = drawRestaurant(it)
                restaurantMapView.setImageBitmap(restaurantMap)
            })
        viewModel.tables.observe(
            viewLifecycleOwner,
            Observer { tablesIds ->
                tablesIds?.let { updateSpinner(it) }
            })
        viewModel.maxLevel.observe(
            viewLifecycleOwner,
            Observer {
                if (it != null) {
                    floor_picker.minValue = 0
                    floor_picker.maxValue = it
                }
            })
        viewModel.selectedDate.observe(
            viewLifecycleOwner,
            Observer {
                it?.let { time_text.text = it }
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
            }
        }

        table_spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
                viewModel.onTableSelected(null)
            }

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                viewModel.onTableSelected(table_spinner.getItemAtPosition(position) as Int)
            }
        }

        email_field.doOnTextChanged { text, _, _, _ ->
            viewModel.setEmail(text.toString())
        }

        book_button.setOnClickListener {
            viewModel.sendBookingRequest()
        }

    }

    private fun updateSpinner(tableIds: List<Int>) {
        val adapter = ArrayAdapter<Int>(
            requireContext(),
            android.R.layout.simple_spinner_item,
            tableIds
        )
        table_spinner.adapter = adapter
        viewModel.onTableSelected(table_spinner.getItemAtPosition(0) as Int)
    }

    companion object {
        const val EXTRA_PLACE_ID = "extra.place.id"
    }
}