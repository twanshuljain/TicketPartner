package com.example.ticketpartner.feature_scan_module

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.ticketpartner.R
import com.example.ticketpartner.common.HYPHEN_CHAR
import com.example.ticketpartner.common.SCAN_MODULE_EVENT_DETAILS
import com.example.ticketpartner.common.SnackBarUtil
import com.example.ticketpartner.common.VERTICAL_POLE
import com.example.ticketpartner.common.ZERO
import com.example.ticketpartner.common.storage.MyPreferences
import com.example.ticketpartner.databinding.FragmentScanQRLandingBinding
import com.example.ticketpartner.databinding.LayoutEndScanBottomDialogBinding
import com.example.ticketpartner.feature_scan_module.feature_login_scan.domain.model.DataItem
import com.example.ticketpartner.feature_scan_module.feature_login_scan.domain.model.Event
import com.example.ticketpartner.feature_scan_module.feature_login_scan.domain.model.EventDates
import com.example.ticketpartner.feature_scan_module.feature_login_scan.domain.model.EventDetailsScanUIState
import com.example.ticketpartner.feature_scan_module.feature_qr_scan.presentation.QrScanViewModel
import com.example.ticketpartner.feature_scan_module.feature_qr_scan.presentation.ScanBottomNavHomeFragment
import com.example.ticketpartner.feature_scan_module.feature_qr_scan.presentation.ScanBottomNavSearchFragment
import com.example.ticketpartner.feature_scan_module.feature_qr_scan.presentation.ScanBottomQRScanFragment
import com.example.ticketpartner.utils.DialogProgressUtil
import com.example.ticketpartner.utils.getFormattedStartDateForEvent
import com.example.ticketpartner.utils.getFormattedTimeForEvent
import com.google.android.material.bottomsheet.BottomSheetDialog

class ScanQRLandingFragment : Fragment() {
    private lateinit var binding: FragmentScanQRLandingBinding
    private val viewModel: QrScanViewModel by activityViewModels()

    private var eventDetailsResponse = ArrayList<DataItem>()
    private var eventData = ArrayList<Event?>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentScanQRLandingBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        arguments?.let {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                eventDetailsResponse = it.getStringArrayList(
                    SCAN_MODULE_EVENT_DETAILS
                ) as ArrayList<DataItem>

            } else {
                eventDetailsResponse =
                    it.getStringArrayList(SCAN_MODULE_EVENT_DETAILS) as ArrayList<DataItem>
            }
            viewModel.putSelectedTicketName(eventDetailsResponse)
        }
        initView()
        initBottomNavigation()
        makeEventDetailsAPICall()
    }

    private fun initBottomNavigation() {
        binding.includeTitle.ivBack.visibility = View.GONE
        binding.includeTitle.title.text = getString(R.string.select_ticket_type)
        binding.scanBottomNav.itemIconTintList = null
        loadFragment(ScanBottomNavHomeFragment())

        binding.scanBottomNav.setOnItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.scanBottomNavHome -> {
                    binding.includeTitle.subTitle.visibility = View.VISIBLE
                    binding.includeTitle.ivBack.visibility = View.GONE
                    binding.includeTitle.title.text = getString(R.string.select_ticket_type)
                    loadFragment(ScanBottomNavHomeFragment())
                    true
                }

                R.id.scanBottomNavQR -> {
                    binding.includeTitle.subTitle.visibility = View.VISIBLE
                    binding.includeTitle.ivBack.visibility = View.VISIBLE
                    binding.includeTitle.title.text = eventData[ZERO]?.name.toString()
                    loadFragment(ScanBottomQRScanFragment())
                    true
                }

                R.id.scanBottomNavSearch -> {
                    binding.includeTitle.subTitle.visibility = View.VISIBLE
                    binding.includeTitle.ivBack.visibility = View.VISIBLE
                    binding.includeTitle.title.text = eventData[ZERO]?.name.toString()
                    loadFragment(ScanBottomNavSearchFragment())
                    true
                }

                else -> false
            }
        }

        viewModel.onContinueClick.observe(viewLifecycleOwner) {
           when(it){
               R.id.scanBottomNavQR -> binding.scanBottomNav.selectedItemId = it
               R.id.qrScanReportFragment -> {
                   binding.includeTitle.title.text = getString(R.string.scanReport)
                   binding.includeTitle.subTitle.visibility = View.GONE
                   binding.includeTitle.ivBack.visibility = View.INVISIBLE
               }
           }
        }

    }

    private fun loadFragment(fragment: Fragment) {
        val transaction = requireActivity().supportFragmentManager.beginTransaction()
        transaction.setCustomAnimations(R.anim.slide_in, R.anim.slide_out)
        transaction.replace(R.id.frameLayout, fragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }

    private fun initView() {
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            if (binding.scanBottomNav.selectedItemId != R.id.scanBottomNavHome) {
                binding.scanBottomNav.selectedItemId = R.id.scanBottomNavHome
            } else {
               // logoutDialog()
            }

        }
        /// navController = Navigation.findNavController(this, R.id.nav_host_fragment)
        binding.includeTitle.ivBack.setOnClickListener {
            if (binding.scanBottomNav.selectedItemId != R.id.scanBottomNavHome) {
                binding.scanBottomNav.selectedItemId = R.id.scanBottomNavHome
            } else {
                //logoutDialog()
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
                    eventData.add(it.onSuccess.data?.event)
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
        binding.includeTitle.subTitle.text = startDate+VERTICAL_POLE+startEndTime
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
            val navController = findNavController()
            navController.popBackStack(R.id.qr_scan_module_navigation, true)
            val intent = Intent(requireActivity(),QrScanModuleActivity::class.java)
            startActivity(intent)
            requireActivity().finish()
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
}