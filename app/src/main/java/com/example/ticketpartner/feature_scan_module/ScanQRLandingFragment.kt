package com.example.ticketpartner.feature_scan_module

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.example.ticketpartner.R
import com.example.ticketpartner.common.SCAN_MODULE_EVENT_DETAILS
import com.example.ticketpartner.common.storage.MyPreferences
import com.example.ticketpartner.databinding.FragmentScanQRLandingBinding
import com.example.ticketpartner.databinding.LayoutEndScanBottomDialogBinding
import com.example.ticketpartner.feature_scan_module.feature_login_scan.domain.model.DataItem
import com.example.ticketpartner.feature_scan_module.feature_qr_scan.presentation.QrScanViewModel
import com.example.ticketpartner.utils.NavigationUtil.navigateWithStackClear
import com.google.android.material.bottomsheet.BottomSheetDialog

class ScanQRLandingFragment : Fragment() {
    private lateinit var binding: FragmentScanQRLandingBinding
    private val viewModel: QrScanViewModel by activityViewModels()
    private var eventDetailsResponse = ArrayList<DataItem>()

    private var navController: NavController? = null
    private var appBarConfiguration: AppBarConfiguration? = null

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
        navController = requireActivity().findNavController(R.id.nav_host_qr_scan)

        arguments?.let {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                eventDetailsResponse = it.getStringArrayList(
                    SCAN_MODULE_EVENT_DETAILS
                ) as ArrayList<DataItem>

            } else {
                eventDetailsResponse =
                    it.getStringArrayList(SCAN_MODULE_EVENT_DETAILS) as ArrayList<DataItem>
            }
            // viewModel.putSelectedTicketName(eventDetailsResponse)
        }

        initBottomNavigation()
        initView()


    }

    private fun initBottomNavigation() {
        binding.includeTitle.ivBack.visibility = View.GONE
        binding.scanBottomNav.itemIconTintList = null
        //  loadFragment(ScanBottomNavHomeFragment())


        /** Implement bottom navigation with navGraph */
        // setSupportActionBar(binding.toolbar)
        binding.scanBottomNav.itemIconTintList = null

        val navHostFragment =
            childFragmentManager.findFragmentById(R.id.nav_host_qr_scan) as NavHostFragment
        val navController = navHostFragment.navController

        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.scanBottomNavHome,
                R.id.scanBottomNavQR,
                R.id.scanBottomNavSearch
            )
        )
        navController?.let {
            // setupActionBarWithNavController(navController!!, appBarConfiguration!!)
            binding.scanBottomNav.setupWithNavController(navController!!)
        }

        viewModel.onContinueClick.observe(requireActivity()) {
            it?.let { redirectId ->
                binding.scanBottomNav.selectedItemId = redirectId
            }
        }


    }

    private fun initView() {
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            requireActivity().finish()
        }

        /// navController = Navigation.findNavController(this, R.id.nav_host_fragment)
        /*  binding.includeTitle.ivBack.setOnClickListener {
              logoutDialog()
          }*/
        viewModel.isLogOut.observe(viewLifecycleOwner) {
            it?.let {
                if (it) {
                    logoutDialog()
                }
            }
        }
    }

    private fun logoutDialog() {
        viewModel.isLogOut.value = false
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
            findNavController().navigateWithStackClear(
                R.id.loginScanModuleFragment,
                true,
                R.id.scanQRLandingFragment
            )
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