package com.example.restaurantreservation.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.example.restaurantreservation.R
import com.example.restaurantreservation.ui.viewmodel.MapViewModel
import com.example.restaurantreservation.viewmodel.ViewModelProviderFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.fragment_map.*
import javax.inject.Inject

class MapFragment : DaggerFragment(), OnMapReadyCallback {

    lateinit var viewModel: MapViewModel

    @Inject
    lateinit var providerFactory: ViewModelProviderFactory

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_map, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
        viewModel = ViewModelProvider(this, providerFactory).get(MapViewModel::class.java)
        test_button.setOnClickListener {
            viewModel.initialize()
        }
    }

    override fun onMapReady(googleMap: GoogleMap?) {
        googleMap?.let {
            googleMap.uiSettings?.setAllGesturesEnabled(true)
            googleMap.uiSettings?.isMyLocationButtonEnabled = true
            googleMap.uiSettings?.isZoomControlsEnabled = true
        }
    }
}