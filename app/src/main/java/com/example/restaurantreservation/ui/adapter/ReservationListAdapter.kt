package com.example.restaurantreservation.ui.adapter

import android.view.ViewGroup
import com.example.restaurantreservation.R
import com.example.restaurantreservation.ui.adapter.viewholder.BaseViewHolder
import com.example.restaurantreservation.ui.adapter.viewholder.ReservationViewHolder

class ReservationListAdapter constructor(
    private val onDeleteClickListener: (String)->Unit
) : BaseAdapter<ReservationAdapterModel, BaseViewHolder<ReservationAdapterModel>>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): BaseViewHolder<ReservationAdapterModel> = ReservationViewHolder(
        inflate(parent, R.layout.reservation_view_holder),
        onDeleteClickListener
    )
}

class ReservationAdapterModel(
    val placeId: String,
    val guid: String,
    val datetime: String
)
