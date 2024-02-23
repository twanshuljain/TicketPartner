package com.example.ticketpartner.feature_login.presentation

import android.graphics.Color
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.ColorRes
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.ticketpartner.R
import com.example.ticketpartner.databinding.FragmentSplashLandingBinding
import com.example.ticketpartner.utils.Utility.changeStringColor
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SplashLandingFragment : Fragment() {
    private lateinit var binding: FragmentSplashLandingBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentSplashLandingBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()

    }

    private fun initView() {
        changeTextColor()

        binding.btnNext.setOnClickListener {
            findNavController().navigate(R.id.signInFragment)
        }


    }

    private fun changeTextColor() {
        val originalText = getString(R.string.elevate_your_events)
        val wordsToColor = listOf("Organized")
        val changedString = changeStringColor(originalText,wordsToColor)
        binding.tvElevateYourEvents.text = changedString
    }
}