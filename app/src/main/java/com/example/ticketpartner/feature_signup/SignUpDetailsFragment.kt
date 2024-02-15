package com.example.ticketpartner.feature_signup

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.ticketpartner.databinding.FragmentSignUpDetailsBinding

class SignUpDetailsFragment : Fragment() {
    private lateinit var binding: FragmentSignUpDetailsBinding
    private var verifyEmail = false
    private var verifyMobile = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentSignUpDetailsBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.tvVerify.setOnClickListener {
            verifyEmail = !verifyEmail
            if (verifyEmail) binding.otpLayout.visibility =
                View.VISIBLE else binding.otpLayout.visibility = View.GONE
        }

        binding.tvVerifyMobile.setOnClickListener {
            verifyMobile = !verifyMobile
            if (verifyMobile) binding.otpLayoutPhone.visibility =
                View.VISIBLE else binding.otpLayoutPhone.visibility = View.GONE
        }
    }

}