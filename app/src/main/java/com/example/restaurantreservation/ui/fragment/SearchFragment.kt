package com.example.restaurantreservation.ui.fragment

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.example.restaurantreservation.R
import com.example.restaurantreservation.ui.adapter.SearchResultsAdapter
import com.example.restaurantreservation.ui.viewmodel.SearchViewModel
import com.example.restaurantreservation.viewmodel.ViewModelProviderFactory
import com.google.android.gms.location.LocationServices
import com.google.android.gms.tasks.Task
import com.google.android.material.tabs.TabLayoutMediator
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.fragment_search.*
import javax.inject.Inject


class SearchFragment : DaggerFragment() {

    private lateinit var viewModel: SearchViewModel

    @Inject
    lateinit var providerFactory: ViewModelProviderFactory

    private var currentLocation: Location? = null

    private lateinit var onPageChangeCallback: ViewPager2.OnPageChangeCallback

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_search, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel =
            ViewModelProvider(requireActivity(), providerFactory).get(SearchViewModel::class.java)
        test_button.setOnClickListener {
            currentLocation?.let { location -> viewModel.searchForNearbyRestaurants(location) }
        }
        setupViewPager()
        fetchLocation()
    }

    private fun setupViewPager() {
        viewpager.adapter = SearchResultsAdapter(this)
        onPageChangeCallback = object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                viewpager.isUserInputEnabled = position != 0
            }
        }
        viewpager.registerOnPageChangeCallback(onPageChangeCallback)
        TabLayoutMediator(tab_layout, viewpager) { tab, position ->
            tab.text = when (position) {
                0 -> getString(R.string.map_tab)
                1 -> getString(R.string.list_tab)
                else -> throw(IllegalArgumentException())
            }
        }.attach()

    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE && grantResults.contains(PackageManager.PERMISSION_GRANTED)) {
            fetchLocation()
        } else {
            Toast.makeText(
                requireContext(),
                "Location is needed for app to work!",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun fetchLocation() {
        if (!locationPermissionsGranted()) {
            requestPermissions(
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                REQUEST_CODE
            )
            return
        }
        val task: Task<Location> =
            LocationServices.getFusedLocationProviderClient(requireActivity()).lastLocation
        task.addOnSuccessListener { location ->
            if (location != null) {
                currentLocation = location
            }
        }
    }

    private fun locationPermissionsGranted() =
        ActivityCompat.checkSelfPermission(
            requireContext(),
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED ||
                ActivityCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED

    companion object {
        const val REQUEST_CODE = 174
    }
}