package com.example.ticketpartner.feature_create_event.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.ticketpartner.R
import com.example.ticketpartner.databinding.FragmentVenueBinding
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng

class VenueFragment : Fragment(), OnMapReadyCallback {
  private lateinit var binding: FragmentVenueBinding
    private lateinit var googleMap: GoogleMap

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentVenueBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
/*

        val mapFragment = requireActivity().supportFragmentManager.findFragmentById(R.id.mapFragment) as SupportMapFragment
        mapFragment.getMapAsync(this)
*/


    }


    override fun onMapReady(map: GoogleMap) {
        if (map != null) {
            googleMap = map
            // Customize map settings, markers, etc.

            // Example: Move camera to a specific location (e.g., Googleplex)
            val googleplex = LatLng(37.7749, 122.4194)
            googleMap.moveCamera(CameraUpdateFactory.newLatLng(googleplex))
            googleMap.animateCamera(CameraUpdateFactory.zoomTo(15.0f))
        }
    }
}