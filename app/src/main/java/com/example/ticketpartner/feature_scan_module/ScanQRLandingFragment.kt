package com.example.ticketpartner.feature_scan_module

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.example.ticketpartner.R
import com.example.ticketpartner.common.HYPHEN_CHAR
import com.example.ticketpartner.common.SnackBarUtil
import com.example.ticketpartner.common.VERTICAL_POLE
import com.example.ticketpartner.common.ZERO
import com.example.ticketpartner.common.storage.MyPreferences
import com.example.ticketpartner.common.storage.PrefConstants
import com.example.ticketpartner.databinding.FragmentScanQRLandingBinding
import com.example.ticketpartner.databinding.LayoutEndScanBottomDialogBinding
import com.example.ticketpartner.feature_scan_module.feature_login_scan.domain.model.EventDates
import com.example.ticketpartner.feature_scan_module.feature_login_scan.domain.model.EventDetailsScanUIState
import com.example.ticketpartner.feature_scan_module.feature_qr_scan.presentation.QrScanViewModel
import com.example.ticketpartner.utils.BackPressHandler
import com.example.ticketpartner.utils.DialogProgressUtil
import com.example.ticketpartner.utils.NavigateFragmentUtil.navigateWithClearNavGraph
import com.example.ticketpartner.utils.getFormattedStartDateForEvent
import com.example.ticketpartner.utils.getFormattedTimeForEvent
import com.google.android.material.bottomsheet.BottomSheetDialog

class ScanQRLandingFragment : Fragment() {
    private lateinit var binding: FragmentScanQRLandingBinding
    private val viewModel: QrScanViewModel by activityViewModels()
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var navController: NavController

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentScanQRLandingBinding.inflate(layoutInflater)

        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                handleOnBackPressedButton()
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(requireActivity(), callback)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initBottomNavigation()
        initView()
        makeEventDetailsAPICall()
    }

    private fun initBottomNavigation() {
        binding.includeTitle.title.text = getString(R.string.select_ticket_type)
        binding.scanBottomNav.itemIconTintList = null

        val navHostFragment =
            childFragmentManager.findFragmentById(R.id.nav_host_qr_scan) as NavHostFragment
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.scanBottomNavHomeFragment,
                R.id.scanBottomQRScanFragment,
                R.id.scanBottomNavSearchFragment,
                R.id.qrScanReportFragment,
                R.id.scanSearchedOrderDetailsFragment
            )
        )
        navController = navHostFragment.navController
        binding.scanBottomNav.setupWithNavController(navController)

        binding.scanBottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.scanBottomQRScanFragment -> {
                    // Apply the condition only for the QR Scan tab
                    val selectedTicketTypeListSize =
                        MyPreferences.getArrayList(PrefConstants.SCAN_SELECTED_TICKET_TYPES_LIST)
                    if (selectedTicketTypeListSize.size > ZERO) {
                        navController.navigate(R.id.scanBottomQRScanFragment)
                        true
                    } else {
                        SnackBarUtil.showErrorSnackBar(
                            binding.root,
                            getString(R.string.pleases_select_ticket_types)
                        )
                        false
                    }
                }

                else -> {
                    navController.navigate(item.itemId)
                    true
                }
            }
        }

        binding.scanBottomNav.setOnNavigationItemReselectedListener { }

        binding.includeTitle.ivBack.setOnClickListener {
            handleOnBackPressedButton()
        }

        navController.addOnDestinationChangedListener { controller, destination, arguments ->
            if (destination.id == R.id.scanBottomNavHomeFragment) {
                binding.includeTitle.ivBack.visibility = View.GONE
            } else {
                binding.includeTitle.ivBack.visibility = View.VISIBLE
            }
        }
    }


    private fun initView() {
        viewModel.selectedSearchedItemEmailAdd.observe(viewLifecycleOwner) {
            binding.includeTitle.subTitle.visibility = View.GONE
            binding.includeTitle.title.text = it.toString()
        }

        viewModel.onContinueClick.observe(viewLifecycleOwner) {
            val selectedTicketTypeListSize =
                MyPreferences.getArrayList(PrefConstants.SCAN_SELECTED_TICKET_TYPES_LIST)
            if (selectedTicketTypeListSize.size > ZERO) {
                binding.scanBottomNav.selectedItemId = it
            } else {
                SnackBarUtil.showErrorSnackBar(
                    binding.root,
                    getString(R.string.pleases_select_ticket_types)
                )
            }
        }

        binding.includeTitle.ivLogOut.setOnClickListener {
            logoutDialog()
        }
    }

    private fun makeEventDetailsAPICall() {
        viewModel.getEventDetailsData()
        viewModel.observeScanEventDetailsResponse.observe(viewLifecycleOwner) {
            when (it) {
                is EventDetailsScanUIState.IsLoading -> {
                    DialogProgressUtil.show(childFragmentManager)
                }

                is EventDetailsScanUIState.OnSuccess -> {
                    DialogProgressUtil.dismiss()
                    showDateTimeOnAppBar(it.onSuccess.data?.event_dates)

                }

                is EventDetailsScanUIState.OnFailure -> {
                    DialogProgressUtil.dismiss()
                    SnackBarUtil.showErrorSnackBar(binding.root, it.onFailure)
                }
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun showDateTimeOnAppBar(eventDates: EventDates?) {
        val startDate =
            getFormattedStartDateForEvent(eventDates?.event_start_date)
        val startEndTime =
            getFormattedTimeForEvent(eventDates?.event_start_time) + HYPHEN_CHAR + getFormattedTimeForEvent(
                eventDates?.event_end_time
            )
        binding.includeTitle.subTitle.text = startDate + VERTICAL_POLE + startEndTime
    }

    private fun logoutDialog() {
        val dialog = BottomSheetDialog(requireContext())
        val dialogView = LayoutEndScanBottomDialogBinding.inflate(layoutInflater)
        dialogView.apply {
            tvTitle.text = getString(R.string.logout)
            tvDescription.text = getString(R.string.are_you_sure_logout)
        }
        dialogView.btnNo.setOnClickListener {
            dialog.dismiss()
        }
        dialogView.btnYes.setOnClickListener {
            findNavController().navigateWithClearNavGraph(
                R.id.nested_qr_scan_nav_graph,
                R.id.loginScanModuleFragment
            )
            /// requireActivity().deleteDatabase(TP_LOCAL_DATABASE)
            viewModel.clearAndRecreateDatabase(requireActivity())
            MyPreferences.clearpref()
            dialog.dismiss()
        }
        dialogView.ivClose.setOnClickListener {
            dialog.dismiss()
        }
        dialog.setCanceledOnTouchOutside(true)
        dialog.setContentView(dialogView.root)
        dialog.show()
    }

    private fun handleOnBackPressedButton() {
        if (binding.scanBottomNav.selectedItemId == R.id.scanBottomNavHomeFragment) {
            BackPressHandler.onBackPressed(requireActivity())
        } else {
            navController.popBackStack()
        }
    }
}