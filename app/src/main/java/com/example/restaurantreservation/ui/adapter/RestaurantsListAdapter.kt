package com.example.restaurantreservation.ui.adapter

import android.view.ViewGroup
import com.example.restaurantreservation.R
import com.example.restaurantreservation.ui.adapter.viewholder.BaseViewHolder
import com.example.restaurantreservation.ui.adapter.viewholder.RestaurantsViewHolder

class RestaurantsListAdapter constructor(
    private val onClickListener: (String)->Unit
) : BaseAdapter<RestaurantAdapterModel, BaseViewHolder<RestaurantAdapterModel>>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): BaseViewHolder<RestaurantAdapterModel> = RestaurantsViewHolder(
        inflate(parent, R.layout.restaurant_viewholder),
        onClickListener
    )
}

class RestaurantAdapterModel(
    val name: String,
    val imageUrl: String?,
    val rating: Double,
    val openingHours: String,
    val placeId: String
)
