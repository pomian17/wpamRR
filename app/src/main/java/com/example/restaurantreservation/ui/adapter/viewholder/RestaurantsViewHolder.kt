package com.example.restaurantreservation.ui.adapter.viewholder

import android.view.View
import com.bumptech.glide.Glide
import com.example.restaurantreservation.ui.adapter.RestaurantAdapterModel
import kotlinx.android.synthetic.main.restaurant_viewholder.view.*


class RestaurantsViewHolder(
    view: View,
    private val onClickListener: (String) -> Unit
) : BaseViewHolder<RestaurantAdapterModel>(view) {

    override fun bind(model: RestaurantAdapterModel) {
        super.bind(model)
        view.restaurant_name.text = model.name
        model.imageUrl?.let{
            Glide.with(view.context)
                .load(it)
                .centerCrop()
                .into(view.restaurant_image)
        }
        view.restaurant_rating.text = model.rating.toString()
        view.opening_hours.text = model.openingHours
        view.setOnClickListener { onClickListener.invoke(model.name) }
    }
}
