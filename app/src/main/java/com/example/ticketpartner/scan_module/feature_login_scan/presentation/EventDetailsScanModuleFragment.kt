package com.example.ticketpartner.scan_module.feature_login_scan.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.ticketpartner.R
import com.example.ticketpartner.common.COMMA
import com.example.ticketpartner.common.HYPHEN_CHAR
import com.example.ticketpartner.common.SnackBarUtil
import com.example.ticketpartner.databinding.FragmentEventDetailsScanModuleBinding
import com.example.ticketpartner.scan_module.feature_login_scan.domain.model.DataItem
import com.example.ticketpartner.scan_module.feature_login_scan.domain.model.EventDetailsScanUIState
import com.example.ticketpartner.utils.DialogProgressUtil
import com.example.ticketpartner.utils.getFormattedStartDateForEvent
import com.example.ticketpartner.utils.getFormattedTimeForEvent


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
        viewModel.getEventDetailsData()
        viewModel.observeScanEventDetailsResponse.observe(viewLifecycleOwner) {
            when (it) {
                is EventDetailsScanUIState.IsLoading -> {
                    DialogProgressUtil.show(childFragmentManager)
                }

                is EventDetailsScanUIState.OnSuccess -> {
                    DialogProgressUtil.dismiss()
                    showDetailsData(it.onSuccess.data)
                }

                is EventDetailsScanUIState.OnFailure -> {
                    DialogProgressUtil.dismiss()
                    SnackBarUtil.showErrorSnackBar(binding.root, it.onFailure)
                }
            }
        }
    }

    private fun showDetailsData(data: DataItem?) {
        binding.tvEventTitle.text = data?.event?.name
        binding.tvStartDate.text =
            getFormattedStartDateForEvent(data?.event_dates?.event_start_date)
        val startEndTime =
            getFormattedTimeForEvent(data?.event_dates?.event_start_time) + HYPHEN_CHAR + getFormattedTimeForEvent(
                data?.event_dates?.event_end_time
            )
        binding.tvStartEndTime.text = startEndTime
        val location = data?.event_locations
        binding.tvLocation.text =
            location?.city + COMMA + location?.state + COMMA + location?.country
    }

    private fun initView() {
        binding.btnContinue.setOnClickListener {
            findNavController().navigate(R.id.selectTicketTypeScanModuleFragment)
        }
    }
}