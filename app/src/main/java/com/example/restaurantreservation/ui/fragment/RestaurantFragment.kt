package com.example.restaurantreservation.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.restaurantreservation.R
import com.example.restaurantreservation.ui.viewmodel.RestaurantViewModel
import com.example.restaurantreservation.util.RestaurantMapDrawer.drawRestaurant
import com.example.restaurantreservation.viewmodel.ViewModelProviderFactory
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.fragment_restaurant.*
import javax.inject.Inject

class RestaurantFragment : DaggerFragment() {
    private lateinit var viewModel: RestaurantViewModel

    @Inject
    lateinit var providerFactory: ViewModelProviderFactory

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
        viewModel.maxLevel.observe(
            viewLifecycleOwner,
            Observer {
                if (it != null) {
                    floor_picker.minValue = 0
                    floor_picker.maxValue = it
                }
            })

        floor_picker.setOnValueChangedListener { _, _, newVal ->
            viewModel.selectFloor(newVal)
        }
    }

    companion object {
        const val EXTRA_PLACE_ID = "extra.place.id"
    }
}