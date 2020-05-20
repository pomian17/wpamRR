package com.example.restaurantreservation.ui.adapter

import android.os.Parcelable
import android.view.ViewGroup
import com.example.restaurantreservation.R
import com.example.restaurantreservation.ui.adapter.viewholder.BaseViewHolder
import com.example.restaurantreservation.ui.adapter.viewholder.RestaurantViewHolder
import kotlinx.android.parcel.Parcelize

class RestaurantsListAdapter constructor(
    private val onClickListener: (RestaurantAdapterModel) -> Unit
) : BaseAdapter<RestaurantAdapterModel, BaseViewHolder<RestaurantAdapterModel>>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): BaseViewHolder<RestaurantAdapterModel> = RestaurantViewHolder(
        inflate(parent, R.layout.restaurant_viewholder),
        onClickListener
    )
}

@Parcelize
class RestaurantAdapterModel(
    val name: String,
    val imageUrl: String?,
    val rating: Double,
    val openedNow: Boolean?,
    val placeId: String
) : Parcelable
