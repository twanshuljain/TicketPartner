package com.mtp.ticketpartner.scanner.feature_scan_module.feature_login_scan.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.mtp.ticketpartner.scanner.BuildConfig
import com.mtp.ticketpartner.scanner.R
import com.mtp.ticketpartner.scanner.common.COMMA
import com.mtp.ticketpartner.scanner.common.HYPHEN_CHAR
import com.mtp.ticketpartner.scanner.common.SnackBarUtil
import com.mtp.ticketpartner.scanner.common.VERTICAL_POLE
import com.mtp.ticketpartner.scanner.common.storage.MyPreferences
import com.mtp.ticketpartner.scanner.databinding.FragmentEventDetailsScanModuleBinding
import com.mtp.ticketpartner.scanner.feature_scan_module.feature_login_scan.domain.model.DataItem
import com.mtp.ticketpartner.scanner.feature_scan_module.feature_login_scan.domain.model.GetEventDetailsOfflineScanUIState
import com.mtp.ticketpartner.scanner.feature_scan_module.feature_login_scan.domain.model.InsertEventDetailsResponse
import com.mtp.ticketpartner.scanner.utils.CameraUtils.Companion.loadCircularBigImage
import com.mtp.ticketpartner.scanner.utils.CameraUtils.Companion.loadImageFromUrl
import com.mtp.ticketpartner.scanner.utils.DialogProgressUtil
import com.mtp.ticketpartner.scanner.utils.DialogUtils
import com.mtp.ticketpartner.scanner.utils.NavigateFragmentUtil.navigateParentToChildFragment
import com.mtp.ticketpartner.scanner.utils.NavigateFragmentUtil.navigateWithClearNavGraph
import com.mtp.ticketpartner.scanner.utils.Utility
import com.mtp.ticketpartner.scanner.utils.getFormattedStartDateForEvent
import com.mtp.ticketpartner.scanner.utils.getFormattedTimeForEvent

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
        loadCircularBigImage(
            binding.ivOrganizerLogo,
            BuildConfig.AWS_IMAGE_BASE_URL + data?.organizationLogo
        )
        binding.tvEventTitle.text = data?.name
        val startDate = getFormattedStartDateForEvent(data?.eventStartDate)
        val startTime = getFormattedTimeForEvent(data?.eventStartTime)
        val endDate = getFormattedStartDateForEvent(data?.eventEndDate)
        val endTime = getFormattedTimeForEvent(data?.eventEndTime)
        binding.tvStartDateTime.text =
            startDate + VERTICAL_POLE + startTime + HYPHEN_CHAR + endDate + VERTICAL_POLE + endTime

        if (data?.isVirtual == true){
            binding.tvLocation.text = getString(R.string.online)
        } else{
            binding.tvLocation.text =
                data?.city + COMMA + data?.state + COMMA + data?.country
        }
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


        binding.ivLogOut.setOnClickListener {
            logoutDialog("Are you sure you want to log out?")
        }
    }

    private fun logoutDialog(message: String) {
        val bottomSheetDialog = BottomSheetDialog(requireContext())
        val dialog =  DialogUtils.showLogoutDialog(
            context = requireContext(),
            layoutInflater = layoutInflater,
            dialog = bottomSheetDialog,
            message = message
        )
        dialog.btnYes.setOnClickListener {
            bottomSheetDialog.dismiss()
            Utility.clearLocalDatabase(requireActivity())
            MyPreferences.clearpref()
            findNavController().navigateWithClearNavGraph(
                R.id.splashLandingFragment,
                R.id.loginScanModuleFragment
            )
        }
    }
}