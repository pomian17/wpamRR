package com.example.restaurantreservation.ui.adapter.viewholder

import android.animation.ArgbEvaluator
import android.graphics.drawable.GradientDrawable
import android.view.View
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import com.example.restaurantreservation.R
import com.example.restaurantreservation.ui.adapter.RestaurantAdapterModel
import kotlinx.android.synthetic.main.restaurant_viewholder.view.*


class RestaurantViewHolder(
    view: View,
    private val onClickListener: (RestaurantAdapterModel) -> Unit
) : BaseViewHolder<RestaurantAdapterModel>(view) {

    override fun bind(model: RestaurantAdapterModel) {
        super.bind(model)
        view.restaurant_name.text = model.name

        val circularProgressDrawable = CircularProgressDrawable(view.context)
        circularProgressDrawable.strokeWidth = 5f
        circularProgressDrawable.centerRadius = 30f
        circularProgressDrawable.start()

        model.imageUrl?.let {
            Glide.with(view.context)
                .load(it)
                .centerCrop()
                .placeholder(circularProgressDrawable)
                .into(view.restaurant_image)
        }
        view.restaurant_rating.text = model.rating.toString()

        val background = GradientDrawable()
        background.setColor(
            ArgbEvaluator().evaluate(
                ((model.rating.toFloat() - 2) / 3).coerceIn(0.0f, 1.0f),
                view.context.resources.getColor(R.color.rrRed, null),
                view.context.resources.getColor(R.color.rrGreen, null)
            ) as Int
        )
        background.cornerRadius = 3f
        view.restaurant_rating.background = background

        if (model.openedNow == true) {
            view.opened_now.text = view.context.getString(R.string.opened_now)
            view.opened_now.setTextColor(view.context.resources.getColor(R.color.rrGreen, null))
        } else {
            view.opened_now.text = view.context.getString(R.string.currently_closed)
            view.opened_now.setTextColor(view.context.resources.getColor(R.color.rrRed, null))
        }
        view.setOnClickListener { onClickListener.invoke(model) }
    }
}
