package com.example.ticketpartner.feature_login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.ticketpartner.R
import com.example.ticketpartner.databinding.FragmentSignInBinding

class SignInFragment : Fragment() {
    private lateinit var binding: FragmentSignInBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentSignInBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        /** Show email/phone layout based on radio button click */
        binding.radioGroup.setOnCheckedChangeListener { _, checkedId ->
            if (checkedId != -1) {
                when (checkedId) {
                    R.id.radioEmail -> {
                        //Toast.makeText(requireContext(), "email", Toast.LENGTH_LONG).show()
                        binding.emailLoginLayout.containEmailLogin.visibility = View.VISIBLE
                        binding.phoneLoginLayout.containMobileLogin.visibility = View.GONE
                    }

                    R.id.radioPhone -> {
                        binding.phoneLoginLayout.containMobileLogin.visibility = View.VISIBLE
                        binding.emailLoginLayout.containEmailLogin.visibility = View.GONE
                    }
                }
            }
        }

        /** Redirect on previous page on back icon click */
        binding.ivBack.setOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }

        binding.tvSignUp.setOnClickListener {
            findNavController().navigate(R.id.signUpEmailFragment)
        }

        binding.emailLoginLayout.tvForgotPassword.setOnClickListener {
            findNavController().navigate(R.id.forgotPasswordFragment)
        }
    }
}