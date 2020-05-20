package com.example.restaurantreservation.ui.adapter.viewholder

import android.text.format.DateFormat
import android.view.View
import com.example.restaurantreservation.ui.adapter.ReservationAdapterModel
import kotlinx.android.synthetic.main.reservation_view_holder.view.*
import java.util.*

class ReservationViewHolder(
    view: View,
    private val onClickListener: (String) -> Unit
) : BaseViewHolder<ReservationAdapterModel>(view) {

    override fun bind(model: ReservationAdapterModel) {
        super.bind(model)
        view.cancel_button.isEnabled = true
        view.restaurant_name.text = model.restaurantName
        view.reservation_month.text = DateFormat.format("MMM", Date(model.datetime)).toString()
        view.reservation_day.text = DateFormat.format("dd", Date(model.datetime)).toString()
        view.reservation_hour.text = DateFormat.format("hh:mm aaa", Date(model.datetime)).toString()
        view.cancel_button.setOnClickListener {
            onClickListener.invoke(model.guid)
            view.cancel_button.isEnabled = false
        }
    }
}
