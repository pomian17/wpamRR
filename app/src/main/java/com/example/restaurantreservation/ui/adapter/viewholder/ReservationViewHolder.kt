package com.example.restaurantreservation.ui.adapter.viewholder

import android.view.View
import com.example.restaurantreservation.ui.adapter.ReservationAdapterModel
import kotlinx.android.synthetic.main.reservation_view_holder.view.*

class ReservationViewHolder(
    view: View,
    private val onClickListener: (String) -> Unit
) : BaseViewHolder<ReservationAdapterModel>(view) {

    override fun bind(model: ReservationAdapterModel) {
        super.bind(model)
        view.cancel_button.isEnabled = true
        view.restaurant_name.text = model.placeId
        view.reservation_date.text = model.datetime
        view.reservation_guid.text = model.guid
        view.cancel_button.setOnClickListener {
            onClickListener.invoke(model.guid)
            view.cancel_button.isEnabled = false
        }
    }
}
