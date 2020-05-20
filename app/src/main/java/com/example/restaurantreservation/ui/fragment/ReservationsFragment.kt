package com.example.restaurantreservation.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.restaurantreservation.R
import com.example.restaurantreservation.data.local.model.Reservation
import com.example.restaurantreservation.ui.adapter.ReservationAdapterModel
import com.example.restaurantreservation.ui.adapter.ReservationListAdapter
import com.example.restaurantreservation.ui.viewmodel.ReservationsViewModel
import com.example.restaurantreservation.viewmodel.ViewModelProviderFactory
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.fragment_results_list.*
import javax.inject.Inject

class ReservationsFragment : DaggerFragment() {

    private lateinit var viewModel: ReservationsViewModel

    @Inject
    lateinit var providerFactory: ViewModelProviderFactory

    private val adapter = ReservationListAdapter {
        onCancelReservationClicked(it)
    }

    private fun onCancelReservationClicked(reservationGuid: String) {
            AlertDialog.Builder(requireContext())
                .setTitle(R.string.delete_reservation_title)
                .setPositiveButton(R.string.delete_reservation_yes) { _, _ ->
                    viewModel.cancelReservation(reservationGuid)
                }
                .setNegativeButton(R.string.delete_reservation_no) { _, _ ->
                    adapter.notifyDataSetChanged()
                }
                .show()
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
            ViewModelProvider(
                requireActivity(),
                providerFactory
            ).get(ReservationsViewModel::class.java)
        recyclerview.adapter = adapter
        viewModel.reservations.observe(
            viewLifecycleOwner,
            Observer { reservations -> reservations?.let { refreshReservations(it) } }
        )
    }

    private fun refreshReservations(reservations: List<Reservation>) {
        no_reservations_text.isVisible = reservations.isEmpty()
        adapter.setAllItems(
            reservations.map {
                ReservationAdapterModel(
                    it.restaurantName,
                    it.guid,
                    it.date
                )
            })
    }

}