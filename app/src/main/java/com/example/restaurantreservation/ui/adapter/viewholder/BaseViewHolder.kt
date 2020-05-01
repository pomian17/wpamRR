package com.example.restaurantreservation.ui.adapter.viewholder

import android.view.View
import androidx.annotation.CallSuper


open class BaseViewHolder<T>(val view: View) :
    androidx.recyclerview.widget.RecyclerView.ViewHolder(view) {

    var model: T? = null
        private set

    @CallSuper
    open fun bind(model: T) {
        this.model = model
    }
}