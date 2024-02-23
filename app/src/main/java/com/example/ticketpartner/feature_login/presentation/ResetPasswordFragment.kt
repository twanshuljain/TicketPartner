package com.example.ticketpartner.feature_login.presentation

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.ticketpartner.R
import com.example.ticketpartner.databinding.FragmentResetPasswordBinding
import com.example.ticketpartner.utils.NavigateFragmentUtil.clearBackStackToDestination
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ResetPasswordFragment : Fragment() {
    private lateinit var binding: FragmentResetPasswordBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentResetPasswordBinding.inflate(layoutInflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()
    }

    private fun initView() {
        binding.btnReset.setOnClickListener {
            //findNavController().navigate(R.id.signInFragment)
            val isValid = checkValidation()
        }

        binding.ivBack.setOnClickListener{
            findNavController().clearBackStackToDestination(R.id.forgotPasswordFragment)
        }
    }

    private fun checkValidation(): Boolean {
        val newPassword = binding.etPass.toString()
        val conPassword = binding.etConfirmPass.toString()
        return if (newPassword.isEmpty()) {
            false
        } else !conPassword.isEmpty()
    }
}