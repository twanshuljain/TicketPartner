package com.example.ticketpartner.feature_create_event.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.ticketpartner.R
import com.example.ticketpartner.common.SnackBarUtil
import com.example.ticketpartner.databinding.FragmentVenueBinding
import com.example.ticketpartner.feature_add_organization.domain.model.SearchCountryResponse
import com.example.ticketpartner.feature_add_organization.domain.model.SearchCountryUIState
import com.example.ticketpartner.feature_create_event.domain.model.CreateEventVenueStateResponse
import com.example.ticketpartner.feature_create_event.domain.model.CreateEventVenueStateUIState
import com.example.ticketpartner.feature_create_event.presentation.adapter.CreateEventVenueStateAdapter
import com.example.ticketpartner.feature_create_event.presentation.adapter.VenueCountryAdapter
import com.example.ticketpartner.utils.DialogProgressUtil
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class VenueFragment : Fragment(), OnMapReadyCallback {
    private val viewModel: CreateEventViewModel by activityViewModels()
    private lateinit var binding: FragmentVenueBinding
    private lateinit var googleMap: GoogleMap

    private var countryListResponse: ArrayList<SearchCountryResponse> = ArrayList()
    private var countryNameListResponse: ArrayList<String> = ArrayList()

    private var stateListResponse: ArrayList<CreateEventVenueStateResponse> = ArrayList()
    private var stateNameListResponse: ArrayList<String> = ArrayList()

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
        initView()
        makeCallGetAPI()
        observeApiResponse()

        /*  val mapFragment =
              requireActivity().supportFragmentManager.findFragmentById(R.id.mapFragment) as SupportMapFragment
          mapFragment.getMapAsync(this)*/
    }

    private fun initView() {
        binding.spinnerCountry.hint = getString(R.string.select_country)
        binding.spinnerState.hint = getString(R.string.select_state)
    }

    private fun makeCallGetAPI() {
        viewModel.getCountryList()
    }

    private fun observeApiResponse() {
        /** observe country list response */
        viewModel.getSearchCountry.observe(viewLifecycleOwner) {
            when (it) {
                is SearchCountryUIState.IsLoading -> {
                    DialogProgressUtil.show(childFragmentManager)
                }

                is SearchCountryUIState.OnSuccess -> {
                    DialogProgressUtil.dismiss()
                    for (i in 0 until it.result.data?.size!!) {
                        countryListResponse.add(it.result)
                        countryNameListResponse.add(it.result.data[i]?.country_name.toString())
                    }
                    setCountryDropDown()
                }

                is SearchCountryUIState.OnFailure -> {
                    DialogProgressUtil.dismiss()
                    SnackBarUtil.showErrorSnackBar(binding.root, it.onFailure)
                }
            }
        }

        /** observe stater api response based on country id */
        viewModel.getStateResponse.observe(viewLifecycleOwner) {
            when (it) {
                is CreateEventVenueStateUIState.IsLoading -> {
                    DialogProgressUtil.show(childFragmentManager)
                }

                is CreateEventVenueStateUIState.OnSuccess -> {
                    DialogProgressUtil.dismiss()
                    for (i in 0 until it.result.data?.size!!) {
                        stateListResponse.add(it.result)
                        stateNameListResponse.add(it.result.data[i]?.state_name.toString())
                    }
                    setStateDropDown()
                }

                is CreateEventVenueStateUIState.OnFailure -> {
                    DialogProgressUtil.dismiss()
                }
            }
        }
    }

    private fun setStateDropDown() {
        val adapter =
            CreateEventVenueStateAdapter(
                requireContext(),
                R.layout.custom_drop_down_layout,
                stateNameListResponse
            )
        binding.spinnerState.setAdapter(adapter)
        binding.spinnerState.setOnItemClickListener { _, _, position, _ ->
            val selectedItem = adapter.getItem(position)
            val selectedItemId = stateListResponse[0].data?.get(position)?.id

            Toast.makeText(
                requireContext(),
                "Selected Item ID: $selectedItemId",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun setCountryDropDown() {
        val adapter =
            VenueCountryAdapter(
                requireContext(),
                R.layout.custom_drop_down_layout,
                countryNameListResponse
            )
        binding.spinnerCountry.setAdapter(adapter)
        binding.spinnerCountry.setOnItemClickListener { _, _, position, _ ->
            val selectedItem = adapter.getItem(position)
            val selectedItemId = countryListResponse[0].data?.get(position)?.id
            selectedItemId?.let {
                viewModel.getStateBasedOnCountry(countryId = it)
            }
            Toast.makeText(
                requireContext(),
                "Selected Item ID: $selectedItemId",
                Toast.LENGTH_SHORT
            ).show()
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