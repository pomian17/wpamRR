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
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import com.example.restaurantreservation.R
import com.example.restaurantreservation.data.model.places.Restaurant
import com.example.restaurantreservation.ui.adapter.RestaurantAdapterModel
import com.example.restaurantreservation.ui.adapter.RestaurantsListAdapter
import com.example.restaurantreservation.ui.adapter.viewholder.RestaurantViewHolder
import com.example.restaurantreservation.ui.fragment.RestaurantFragment.Companion.EXTRA_PLACE_ID
import com.example.restaurantreservation.ui.viewmodel.SearchViewModel
import com.example.restaurantreservation.util.MarkerDrawer
import com.example.restaurantreservation.viewmodel.ViewModelProviderFactory
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.google.android.gms.tasks.Task
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.fragment_map.*
import javax.inject.Inject


class MapFragment : DaggerFragment(), OnMapReadyCallback {

    private lateinit var viewModel: SearchViewModel

    @Inject
    lateinit var providerFactory: ViewModelProviderFactory

    private var currentLocation: Location? = null

    private var googleMap: GoogleMap? = null

    private val adapter = RestaurantsListAdapter {
        val bundle = bundleOf(EXTRA_PLACE_ID to it)
        findNavController().navigate(R.id.action_searchFragment_to_restaurantFragment, bundle)
    }

    private val markers = mutableListOf<Marker>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_map, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val snapHelper = PagerSnapHelper()
        recyclerview.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        snapHelper.attachToRecyclerView(recyclerview)
        recyclerview.adapter = adapter
        recyclerview.setOnScrollChangeListener { _, _, _, _, _ ->
            animateCameraToSelectedMarker()
        }
        viewModel =
            ViewModelProvider(requireActivity(), providerFactory).get(SearchViewModel::class.java)
        fetchLocation()
        viewModel.restaurants.observe(
            viewLifecycleOwner,
            Observer { restaurants -> restaurants?.let { refreshRestaurants(it) } }
        )
    }

    private fun animateCameraToSelectedMarker() {
        val visibleView = recyclerview.getChildAt(0)
        if (visibleView.x > 0) {
            val visibleViewHolder =
                recyclerview.getChildViewHolder(visibleView) as RestaurantViewHolder
            val visibleRestaurantId = visibleViewHolder.model!!.placeId
            markers.firstOrNull { it.tag == visibleRestaurantId }?.let { marker ->
                marker.showInfoWindow()
                googleMap?.animateCamera(CameraUpdateFactory.newLatLng(marker.position))
            }
        }
    }

    private fun refreshRestaurants(restaurants: List<Restaurant>) {
        refreshRestaurantsOnMap(restaurants)
        refreshRestaurantsViewPager(restaurants)
    }

    private fun refreshRestaurantsOnMap(restaurants: List<Restaurant>) {
        googleMap?.clear()
        markers.clear()
        restaurants.forEach { restaurant ->
            restaurant.geometry?.location?.let {
                googleMap?.addMarker(
                    MarkerOptions()
                        .position(LatLng(it.latitude, it.longitude))
                        .icon(
                            BitmapDescriptorFactory.fromBitmap(
                                MarkerDrawer.drawMarker(
                                    restaurant.rating,
                                    requireContext()
                                )
                            )
                        )
                )?.apply {
                    tag = restaurant.placeId
                    markers.add(this)
                }
            }
        }
    }

    private fun refreshRestaurantsViewPager(restaurants: List<Restaurant>) {
        val bottomMapPadding = if (restaurants.isNotEmpty()) {
            recyclerview.height
        } else {
            0
        }
        googleMap?.setPadding(0, 0, 0, bottomMapPadding)
        adapter.setAllItems(
            restaurants.map {
                RestaurantAdapterModel(
                    it.name,
                    it.photos?.first()?.getPhotoRequest(),
                    it.rating,
                    it.openingHours?.openNow.toString(),
                    it.placeId
                )
            })
        recyclerview.isVisible = restaurants.isNotEmpty()
    }

    override fun onMapReady(googleMap: GoogleMap?) {
        this.googleMap = googleMap
        val latLng = currentLocation?.let { LatLng(it.latitude, it.longitude) }
        googleMap?.apply {
            setMapStyle(
                MapStyleOptions.loadRawResourceStyle(
                    requireContext(), R.raw.map_style
                )
            )
            uiSettings?.setAllGesturesEnabled(true)
            uiSettings?.isMyLocationButtonEnabled = true
            uiSettings?.isZoomControlsEnabled = true
            isMyLocationEnabled = true
            animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, DEFAULT_ZOOM))
            setOnCameraIdleListener { viewModel.setSearchArea(projection.visibleRegion.latLngBounds) }
            setOnMarkerClickListener { marker ->
                val position = adapter.items.indexOfFirst { it.placeId == marker.tag }
                recyclerview.scrollToPosition(position)
                false
            }
        }
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
                val mapFragment = childFragmentManager
                    .findFragmentById(R.id.map) as SupportMapFragment
                mapFragment.getMapAsync(this)
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
        const val REQUEST_CODE = 175
        const val DEFAULT_ZOOM = 14f
    }
}