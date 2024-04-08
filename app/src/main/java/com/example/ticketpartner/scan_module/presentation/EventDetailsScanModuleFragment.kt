package com.example.ticketpartner.scan_module.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.ticketpartner.databinding.FragmentEventDetailsScanModuleBinding


class EventDetailsScanModuleFragment : Fragment() {
    private lateinit var binding: FragmentEventDetailsScanModuleBinding
    private val viewModel: LoginScanVewModel by activityViewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentEventDetailsScanModuleBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        getEventDetailsResponse()
    }

    private fun getEventDetailsResponse() {
        viewModel.observePinLoginResponse
    }

    private fun initView() {
        /// binding.materialCardView.background = requireContext().getDrawable(R.drawable.corner_curve_layout_design_orange_border)
    }

}