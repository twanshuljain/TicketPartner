package com.example.ticketpartner

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import com.example.ticketpartner.databinding.FragmentScanQRLandingBinding
import com.example.ticketpartner.scan_module.presentation.ScanBottomNavHomeFragment
import com.example.ticketpartner.scan_module.presentation.ScanBottomNavSearchFragment
import com.example.ticketpartner.scan_module.presentation.ScanBottomQRScanFragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class ScanQRLandingFragment : Fragment() {
    private lateinit var binding: FragmentScanQRLandingBinding
    private lateinit var bottomNavView: BottomNavigationView
    private lateinit var navController: NavController
    private var isTorchOn = false
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
        initBottomNavigation()
        initView()
    }

    private fun initBottomNavigation() {
        loadFragment(ScanBottomQRScanFragment())
        binding.scanBottomNav.setOnItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.scanBottomNavHome -> {
                    loadFragment(ScanBottomNavHomeFragment())
                    true
                }

                R.id.scanBottomNavQR -> {
                    loadFragment(ScanBottomQRScanFragment())
                    true
                }

                R.id.scanBottomNavSearch -> {
                    loadFragment(ScanBottomNavSearchFragment())
                    true
                }

                else -> false
            }
        }

    }

    private fun loadFragment(fragment: Fragment) {
        val transaction = requireActivity().supportFragmentManager.beginTransaction()
        transaction.replace(R.id.frameLayout, fragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }

    private fun initView() {
        /// navController = Navigation.findNavController(this, R.id.nav_host_fragment)
        binding.includeTitle.ivBack.setOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }
        /*
                binding.ivTorch.setOnClickListener {
                    if (isTorchOn) {
                        TorchController.turnOff(requireContext())
                        isTorchOn = false
                    } else {
                        TorchController.turnOn(requireContext())
                        isTorchOn = true
                    }
                }*/
    }
}