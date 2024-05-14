package com.example.ticketpartner.feature_scan_module.feature_login_scan.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.ticketpartner.R
import com.example.ticketpartner.common.COMMA
import com.example.ticketpartner.common.HYPHEN_CHAR
import com.example.ticketpartner.common.SnackBarUtil
import com.example.ticketpartner.common.VERTICAL_POLE
import com.example.ticketpartner.databinding.FragmentEventDetailsScanModuleBinding
import com.example.ticketpartner.feature_scan_module.feature_login_scan.domain.model.DataItem
import com.example.ticketpartner.feature_scan_module.feature_login_scan.domain.model.EventDetailsScanUIState
import com.example.ticketpartner.utils.DialogProgressUtil
import com.example.ticketpartner.utils.NavigateFragmentUtil.navigateParentToChildFragment
import com.example.ticketpartner.utils.getFormattedStartDateForEvent
import com.example.ticketpartner.utils.getFormattedTimeForEvent

class EventDetailsScanModuleFragment : Fragment() {
    private lateinit var binding: FragmentEventDetailsScanModuleBinding
    private val viewModel: LoginScanVewModel by activityViewModels()
    private val eventDetails = ArrayList<DataItem>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentEventDetailsScanModuleBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

         val callback: OnBackPressedCallback =
              object : OnBackPressedCallback(true) {
                  override fun handleOnBackPressed() {
                      // Leave empty do disable back press or
                      // write your code which you want
                  }
              }
          requireActivity().onBackPressedDispatcher.addCallback(
              requireActivity(),
              callback
          )

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
                    it.onSuccess.data?.let { data -> eventDetails.add(data) }
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
       // binding.ivBanner.setImageBitmap(CameraUtils.uriToBitmap(requireContext(),data?.event.event_cover_image))
        val startDate = getFormattedStartDateForEvent(data?.event_dates?.event_start_date)
        val startEndTime =
            getFormattedTimeForEvent(data?.event_dates?.event_start_time) + HYPHEN_CHAR + getFormattedTimeForEvent(
                data?.event_dates?.event_end_time
            )
        binding.tvStartDateTime.text = startDate + VERTICAL_POLE + startEndTime

        val location = data?.event_locations
        binding.tvLocation.text =
            location?.city + COMMA + location?.state + COMMA + location?.country
        binding.tvOrganizerName.text = data?.organization?.name.toString()
    }

    private fun initView() {
        binding.btnContinue.setOnClickListener {
            findNavController().navigateParentToChildFragment(
                R.id.eventDetailsScanModuleFragment,
                R.id.action_eventDetailsScanModuleFragment_to_nested_qr_scan_nav_graph,
                true
            )
        }
    }
}