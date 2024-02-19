package com.example.ticketpartner.feature_login.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.ticketpartner.R
import com.example.ticketpartner.databinding.FragmentSignInBinding
import com.example.ticketpartner.feature_login.domain.model.ValidationEmailLoginUIState
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class SignInFragment : Fragment() {
    private lateinit var binding: FragmentSignInBinding
    private val viewModel: LoginViewModel by activityViewModels()
    private var etEmail: String = ""
    private var etPassword: String = ""
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
        // viewModel = ViewModelProvider(this)[LoginViewModel::class.java]

        initView()
        setUIStateObserver()

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


    private fun initView() {
        //get email text from user
        binding.emailLoginLayout.etEmail.doAfterTextChanged {
            etEmail = it.toString()
        }

        //get password text from user
        binding.emailLoginLayout.etPassword.doAfterTextChanged {
            etPassword = it.toString()
        }

        binding.btnSignIn.setOnClickListener {
            //login with email
            // viewModel.emailUserLogin("raj@gmail.com","Raj@1234")

            ///validation
            // viewModel.isAllFieldValid(etEmail,etPassword)

            //send otp for mobile login
            //viewModel.sendOtpLogin("+91","9644756516")

            //login by mobile number
            viewModel.loginWithMobileNumber("+91", "9644756516", "4321")
        }

    }

    private fun setUIStateObserver() {

        /*viewModel.loginUiState.observe(requireActivity()){
            when(it){
                is LoginUiState.Loading -> {

                }
                is LoginUiState.OnSuccess -> {}

                is LoginUiState.OnAllDataValid -> {}

                is LoginUiState.OnError -> {}

                is LoginUiState.OnEmailEnter -> {}

            }
        }*/

        viewModel.emailLoginUiState.observe(viewLifecycleOwner) {
            when (it) {
                is ValidationEmailLoginUIState.Loading -> {}
                is ValidationEmailLoginUIState.EmailIsNotValid -> {
                    Toast.makeText(requireContext(), "email is not valid", Toast.LENGTH_SHORT)
                        .show()
                }

                is ValidationEmailLoginUIState.AllFieldsRequired -> {
                    Toast.makeText(requireContext(), "all fields required", Toast.LENGTH_SHORT)
                        .show()
                }

                is ValidationEmailLoginUIState.OnAllDataValid -> {
                    Toast.makeText(requireContext(), "all data valid", Toast.LENGTH_SHORT).show()

                }

                is ValidationEmailLoginUIState.IsPasswordEmpty -> {
                    Toast.makeText(requireContext(), "password is empty", Toast.LENGTH_SHORT).show()
                }

                is ValidationEmailLoginUIState.OnEmailEnter -> {

                }
            }
        }
    }
}