package com.example.ticketpartner.scan_module

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.example.ticketpartner.R
import com.example.ticketpartner.common.SCAN_MODULE_EVENT_DETAILS
import com.example.ticketpartner.common.storage.MyPreferences
import com.example.ticketpartner.databinding.FragmentScanQRLandingBinding
import com.example.ticketpartner.databinding.LayoutEndScanBottomDialogBinding
import com.example.ticketpartner.scan_module.feature_login_scan.domain.model.DataItem
import com.example.ticketpartner.scan_module.feature_qr_scan.presentation.QrScanViewModel
import com.example.ticketpartner.scan_module.feature_qr_scan.presentation.ScanBottomNavHomeFragment
import com.example.ticketpartner.scan_module.feature_qr_scan.presentation.ScanBottomNavSearchFragment
import com.example.ticketpartner.scan_module.feature_qr_scan.presentation.TicketScannedStatusFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.bottomsheet.BottomSheetDialog

class ScanQRLandingFragment : Fragment() {
    private lateinit var binding: FragmentScanQRLandingBinding
    private val viewModel: QrScanViewModel by activityViewModels()
    private lateinit var bottomNavView: BottomNavigationView
    private lateinit var navController: NavController
    private var eventDetailsResponse = ArrayList<DataItem>()

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

        initBottomNavigation()
        initView()
    }

    private fun initBottomNavigation() {
        binding.includeTitle.ivBack.visibility = View.GONE
        binding.scanBottomNav.itemIconTintList = null
        loadFragment(ScanBottomNavHomeFragment())
        binding.scanBottomNav.setOnItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.scanBottomNavHome -> {
                    binding.includeTitle.ivBack.visibility = View.GONE
                    loadFragment(ScanBottomNavHomeFragment())
                    true
                }

                R.id.scanBottomNavQR -> {
                    binding.includeTitle.ivBack.visibility = View.VISIBLE
                    //loadFragment(ScanBottomQRScanFragment())
                    loadFragment(TicketScannedStatusFragment())
                    true
                }

                R.id.scanBottomNavSearch -> {
                    binding.includeTitle.ivBack.visibility = View.VISIBLE
                    loadFragment(ScanBottomNavSearchFragment())
                    true
                }

                else -> false
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
            logoutDialog()
        }
        /// navController = Navigation.findNavController(this, R.id.nav_host_fragment)
        binding.includeTitle.ivBack.setOnClickListener {
            logoutDialog()
        }

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
            findNavController().popBackStack(R.id.loginScanModuleFragment,false)
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