package com.mtp.scanner.feature_scan_module.feature_login_scan.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.mtp.scanner.BuildConfig
import com.mtp.scanner.R
import com.mtp.scanner.common.COMMA
import com.mtp.scanner.common.HYPHEN_CHAR
import com.mtp.scanner.common.SnackBarUtil
import com.mtp.scanner.common.VERTICAL_POLE
import com.mtp.scanner.databinding.FragmentEventDetailsScanModuleBinding
import com.mtp.scanner.feature_scan_module.feature_login_scan.domain.model.DataItem
import com.mtp.scanner.feature_scan_module.feature_login_scan.domain.model.GetEventDetailsOfflineScanUIState
import com.mtp.scanner.feature_scan_module.feature_login_scan.domain.model.InsertEventDetailsResponse
import com.mtp.scanner.utils.CameraUtils.Companion.loadCircularImage
import com.mtp.scanner.utils.CameraUtils.Companion.loadImageFromUrl
import com.mtp.scanner.utils.DialogProgressUtil
import com.mtp.scanner.utils.NavigateFragmentUtil.navigateParentToChildFragment
import com.mtp.scanner.utils.getFormattedStartDateForEvent
import com.mtp.scanner.utils.getFormattedTimeForEvent

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
        getEventDetailsLocalStorage()

        val callback: OnBackPressedCallback =
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    // Leave empty do disable back press or
                }
            }
        requireActivity().onBackPressedDispatcher.addCallback(
            requireActivity(),
            callback
        )
        initView()
    }

    private fun getEventDetailsLocalStorage() {
        viewModel.getEventDetailsOfflineScan()
        viewModel.getEventDetailsOfflineScan.observe(viewLifecycleOwner){
            when(it){
                is GetEventDetailsOfflineScanUIState.IsLoading -> {
                    DialogProgressUtil.show(childFragmentManager)
                }
                is GetEventDetailsOfflineScanUIState.OnSuccess -> {
                    DialogProgressUtil.dismiss()
                    showDetailsData(it.onSuccess)
                }
                is GetEventDetailsOfflineScanUIState.OnFailure -> {
                    DialogProgressUtil.dismiss()
                    SnackBarUtil.showCustomSnackBar(binding.root,it.onFailure)
                }
            }
        }
    }

    private fun showDetailsData(data: InsertEventDetailsResponse?) {
        loadImageFromUrl(
            binding.ivBanner,
            BuildConfig.AWS_IMAGE_BASE_URL +data?.eventCoverImage
        )
        loadCircularImage(
            binding.ivOrganizerLogo,
            BuildConfig.AWS_IMAGE_BASE_URL + data?.organizationLogo
        )
        binding.tvEventTitle.text = data?.name
        val startDate = getFormattedStartDateForEvent(data?.eventStartDate)
        val startEndTime =
            getFormattedTimeForEvent(data?.eventStartTime) + HYPHEN_CHAR + getFormattedTimeForEvent(
                data?.eventEndTime
            )
        binding.tvStartDateTime.text = startDate + VERTICAL_POLE + startEndTime

        binding.tvLocation.text =
            data?.city + COMMA + data?.state + COMMA + data?.country
        binding.tvOrganizerName.text = data?.organizationName?.toString()
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