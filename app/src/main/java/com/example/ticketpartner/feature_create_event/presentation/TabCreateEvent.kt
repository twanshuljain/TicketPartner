package com.example.ticketpartner.feature_create_event.presentation

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter

private const val NUM_TABS = 3

class TabCreateEvent(fragmentManager: FragmentManager, lifecycle: Lifecycle) :
    FragmentStateAdapter(fragmentManager, lifecycle) {

    override fun getItemCount(): Int = NUM_TABS

    override fun createFragment(position: Int): Fragment {
        when (position) {
            0 -> return VenueFragment()
            1 -> return OnlineFragment()
        }
        return ToBeAnnouncedFragment()
    }
}