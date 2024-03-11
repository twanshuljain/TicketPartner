package com.example.ticketpartner.feature_create_event.presentation

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.ticketpartner.R
import com.example.ticketpartner.databinding.FragmentToBeAnnouncedBinding
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class ToBeAnnouncedFragment : Fragment(), OnMapReadyCallback {
    private lateinit var binding: FragmentToBeAnnouncedBinding
    private lateinit var googleMap: GoogleMap

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentToBeAnnouncedBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    private fun initView() {
        binding.switchSendAddToConfirmation.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) binding.clLocationMapLayout.visibility =
                View.VISIBLE else binding.clLocationMapLayout.visibility = View.GONE
        }

    }

    override fun onMapReady(map: GoogleMap) {
        val icon = BitmapDescriptorFactory.fromResource(R.drawable.ic_location_pin_purple)
        if (map != null) {
            googleMap = map
            val location = LatLng(37.7749, 122.4194)
            googleMap.addMarker(
                MarkerOptions().position(location).title("You are here!").icon(icon)
            )
            googleMap.moveCamera(CameraUpdateFactory.newLatLng(location))
            googleMap.animateCamera(CameraUpdateFactory.zoomTo(15.0f))
        }
    }
}