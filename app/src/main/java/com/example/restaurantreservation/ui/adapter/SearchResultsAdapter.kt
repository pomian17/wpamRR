package com.example.restaurantreservation.ui.adapter

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.restaurantreservation.ui.fragment.MapFragment
import com.example.restaurantreservation.ui.fragment.ResultsListFragment

class SearchResultsAdapter(parentFragment: Fragment) : FragmentStateAdapter(parentFragment) {

    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment =
        when (position) {
            0 -> MapFragment()
            1 -> ResultsListFragment()
            else -> throw IllegalArgumentException()
        }
}
