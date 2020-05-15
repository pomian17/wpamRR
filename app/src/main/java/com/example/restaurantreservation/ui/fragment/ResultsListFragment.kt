package com.example.restaurantreservation.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.restaurantreservation.R
import com.example.restaurantreservation.data.model.Restaurant
import com.example.restaurantreservation.ui.adapter.RestaurantAdapterModel
import com.example.restaurantreservation.ui.adapter.RestaurantsListAdapter
import com.example.restaurantreservation.ui.viewmodel.SearchViewModel
import com.example.restaurantreservation.viewmodel.ViewModelProviderFactory
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.fragment_results_list.*
import javax.inject.Inject

class ResultsListFragment : DaggerFragment() {

    private lateinit var viewModel: SearchViewModel

    @Inject
    lateinit var providerFactory: ViewModelProviderFactory

    private val adapter = RestaurantsListAdapter {
        Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_results_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel =
            ViewModelProvider(requireActivity(), providerFactory).get(SearchViewModel::class.java)
        recyclerview.adapter = adapter
        viewModel.restaurants.observe(
            viewLifecycleOwner,
            Observer { restaurants -> restaurants?.let { refreshRestaurants(it) } }
        )
    }
    private fun refreshRestaurants(restaurants: List<Restaurant>) {
        adapter.setAllItems(
            restaurants.map {
                RestaurantAdapterModel(
                    it.name,
                    it.photos?.first()?.getPhotoRequest(),
                    it.rating,
                    it.openingHours?.openNow.toString()
                )
            })
    }

}