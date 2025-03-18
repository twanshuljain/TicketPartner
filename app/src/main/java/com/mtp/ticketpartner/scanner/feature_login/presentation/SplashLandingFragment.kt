package com.mtp.ticketpartner.scanner.feature_login.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.mtp.scanner.R
import com.mtp.ticketpartner.scanner.common.storage.MyPreferences
import com.mtp.ticketpartner.scanner.common.storage.PrefConstants
import com.mtp.scanner.databinding.FragmentSplashLandingBinding
import com.mtp.ticketpartner.scanner.utils.NavigateFragmentUtil.navigateWithClearNavGraph
import com.mtp.ticketpartner.scanner.utils.Utility.changeStringColor
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SplashLandingFragment : Fragment() {
    private lateinit var binding: FragmentSplashLandingBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        if (!MyPreferences.getString(PrefConstants.LOGGED_USER_DETAILS).isNullOrEmpty()) {
            findNavController().navigateWithClearNavGraph(R.id.main_nav_graph,R.id.nested_qr_scan_nav_graph)
        }
        binding = FragmentSplashLandingBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    private fun initView() {
       // changeTextColor()
        binding.btnNext.setOnClickListener {
            findNavController().navigate(R.id.loginScanModuleFragment)
        }
    }

    private fun changeTextColor() {
        val originalText = getString(R.string.elevate_your_events)
        val wordsToColor = listOf(requireContext().getString(R.string.organized))
        val changedString = changeStringColor(originalText, wordsToColor)
        binding.tvElevateYourEvents.text = changedString
    }
}