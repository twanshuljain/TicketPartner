package com.example.ticketpartner.feature_scan_module

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.ticketpartner.R
import com.example.ticketpartner.databinding.ActivityScanQrcodeBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ScanQRCodeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityScanQrcodeBinding
    private var navController: NavController? = null
    private  var appBarConfiguration: AppBarConfiguration? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityScanQrcodeBinding.inflate(layoutInflater)
        setContentView(binding.root)


        /** Implement bottom navigation with navGraph */
        setSupportActionBar(binding.toolbar)
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
            setupActionBarWithNavController(navController!!, appBarConfiguration!!)
            binding.scanBottomNav.setupWithNavController(navController!!)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.fragment_bottom_container)
        return navController.navigateUp(appBarConfiguration!!) || super.onSupportNavigateUp()
    }

}