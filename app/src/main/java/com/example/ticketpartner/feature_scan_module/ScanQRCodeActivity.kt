package com.example.ticketpartner.feature_scan_module

import android.content.Intent
import android.os.Bundle
import androidx.activity.OnBackPressedCallback
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.ui.AppBarConfiguration
import com.example.ticketpartner.R
import com.example.ticketpartner.common.EVENT_TICKET_LIST_FROM_DETAILS
import com.example.ticketpartner.common.storage.MyPreferences
import com.example.ticketpartner.databinding.ActivityScanQrcodeBinding
import com.example.ticketpartner.databinding.LayoutEndScanBottomDialogBinding
import com.example.ticketpartner.feature_login.presentation.LoginActivity
import com.example.ticketpartner.feature_scan_module.feature_login_scan.domain.model.EventTicket
import com.example.ticketpartner.feature_scan_module.feature_qr_scan.presentation.QrScanViewModel
import com.google.android.material.bottomsheet.BottomSheetDialog
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ScanQRCodeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityScanQrcodeBinding
    private var navController: NavController? = null
    private var appBarConfiguration: AppBarConfiguration? = null
    val viewModel: QrScanViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityScanQrcodeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initView()

        val eventTickets : ArrayList<EventTicket>? = intent.getParcelableArrayListExtra(EVENT_TICKET_LIST_FROM_DETAILS)
        eventTickets?.let {
            viewModel.putEventTicketList(eventTickets)
        }

        onBackPressedDispatcher.addCallback(
            this /* lifecycle owner */,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    if (navController?.currentDestination?.id == R.id.scanBottomNavHome) {
                        finish()
                    } else {
                        navController?.popBackStack()
                    }
                }
            })

     /*   *//** Implement bottom navigation with navGraph *//*
       // setSupportActionBar(binding.toolbar)
        binding.scanBottomNav.itemIconTintList = null
        navController = findNavController(R.id.fragment_bottom_container)
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
        }*/

        viewModel.onContinueClick.observe(this){
            it?.let {redirectId ->
                binding.scanBottomNav.selectedItemId = redirectId
            }
        }
    }

    private fun initView() {
      /*  binding.includeTitle.ivBack.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        binding.includeTitle.ivLogOut.setOnClickListener {
       //     logoutDialog()
        }*/
    }

  /*  override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.fragment_bottom_container)
        return navController.navigateUp(appBarConfiguration!!) || super.onSupportNavigateUp()
    }*/

    private fun logoutDialog() {
        val dialog = BottomSheetDialog(this)
        val dialogView = LayoutEndScanBottomDialogBinding.inflate(layoutInflater)
        dialogView.apply {
            tvTitle.text = getString(R.string.logout)
            tvDescription.text = getString(R.string.are_you_sure_logout)
        }
        dialogView.btnNo.setOnClickListener {
            dialog.dismiss()
        }
        dialogView.btnYes.setOnClickListener {
            //findNavController().popBackStack(R.id.loginScanModuleFragment,false)
            //findNavController(R.id.scanBottomNavHome).popBackStack(R.id.loginScanModuleFragment,false)
            val intent  = Intent(this,LoginActivity::class.java)
            startActivity(intent)
           // navController?.popBackStack(R.id.loginScanModuleFragment,false)
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