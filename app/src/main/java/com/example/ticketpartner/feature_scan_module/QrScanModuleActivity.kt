package com.example.ticketpartner.feature_scan_module

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import com.example.ticketpartner.R
import com.example.ticketpartner.common.storage.MyPreferences
import com.example.ticketpartner.common.storage.PrefConstants
import com.example.ticketpartner.databinding.ActivityQrScanModuleBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class QrScanModuleActivity : AppCompatActivity() {
    private lateinit var binding: ActivityQrScanModuleBinding
    private lateinit var navController: NavController
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityQrScanModuleBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (!MyPreferences.getString(PrefConstants.LOGGED_USER_DETAILS).isNullOrEmpty()) {
            val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_scan_module) as NavHostFragment
            navController = navHostFragment.navController
            navController.popBackStack(R.id.qr_scan_module_navigation,true)
            navController.navigate(R.id.rqScanNavGraph)
        }
    }
    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_scan_module)
        return navController.navigateUp() || super.onSupportNavigateUp()
    }

 /*   override fun onBackPressed() {
        super.onBackPressed()
        onBackPressedDispatcher.onBackPressed()
    }*/
}