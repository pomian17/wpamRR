package com.example.restaurantreservation.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import com.example.restaurantreservation.ui.adapter.viewholder.BaseViewHolder
import java.util.*

abstract class BaseAdapter<T, R : BaseViewHolder<T>> constructor(
    var items: MutableList<T> = LinkedList()
) : androidx.recyclerview.widget.RecyclerView.Adapter<R>() {

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: R, position: Int) {
        holder.bind(items[position])
    }

    open fun addItem(item: T) {
        this.items.add(item)
        notifyItemInserted(this.items.size - 1)
    }

    open fun clear() {
        this.items.clear()
        this.notifyDataSetChanged()
    }

    open fun removeItem(index: Int): T {
        val removedItem = items.removeAt(index)
        notifyItemRemoved(index)
        return removedItem
    }

    open fun addItem(location: Int, item: T) {
        items.add(location, item)
        notifyItemInserted(location)
    }

    open fun addItems(itemsToAdd: List<T>?) {
        if (itemsToAdd == null || itemsToAdd.isEmpty()) {
            return
        }
        val count = itemsToAdd.size
        val originalSize = items.size
        items.addAll(itemsToAdd)
        notifyItemRangeInserted(originalSize, count)
    }

    open fun addItems(location: Int, itemsToAdd: List<T>) {
        val count = itemsToAdd.size
        val originalSize = items.size
        items.addAll(location, itemsToAdd)
        notifyItemRangeInserted(originalSize, count)
    }

    open fun setAllItems(itemsToSet: List<T>?) {
        this.items.clear()
        if (itemsToSet != null) {
            this.items.addAll(itemsToSet)
        }
        notifyDataSetChanged()
    }

    protected fun inflate(viewGroup: ViewGroup, @LayoutRes layoutRes: Int): View {
        return LayoutInflater.from(viewGroup.context)
            .inflate(layoutRes, viewGroup, false)
    }
}

interface OnItemSelectedListener<T> {
    fun onItemSelected(item: T)
}
