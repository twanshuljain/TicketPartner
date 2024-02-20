package com.example.ticketpartner.feature_login.presentation


import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.ticketpartner.R
import com.example.ticketpartner.common.IndianCountryCode
import com.example.ticketpartner.common.PLUS
import com.example.ticketpartner.common.SnackBarUtil
import com.example.ticketpartner.databinding.FragmentSignInBinding
import com.example.ticketpartner.databinding.LoginMobileNumberBinding
import com.example.ticketpartner.feature_login.domain.model.EmailLoginUIState
import com.example.ticketpartner.feature_login.domain.model.SendMobileOtpUIState
import com.example.ticketpartner.feature_login.domain.model.ValidationEmailLoginUIState
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class SignInFragment : Fragment() {
    private lateinit var binding: FragmentSignInBinding
    private val viewModel: LoginViewModel by activityViewModels()
    private var etEmail: String = ""
    private var etPassword: String = ""
    private var etPhone: String = ""
    private var countryCode: String = IndianCountryCode
    private var otoFirst: String = ""
    private var otpSec: String = ""
    private var otpThird: String = ""
    private var otpFourth: String = ""
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
        val mobileLayout = binding.phoneLoginLayout

        /** get email text from user */
        binding.emailLoginLayout.etEmail.doAfterTextChanged {
            etEmail = it.toString()
        }

        /** get password text from user */
        binding.emailLoginLayout.etPassword.doAfterTextChanged {
            etPassword = it.toString()
        }


        mobileLayout.otp1.setOnEditorActionListener { _, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_NEXT || event.keyCode == KeyEvent.KEYCODE_ENTER) {
                // Move focus to editText2 when "Enter" is pressed
                mobileLayout.otp2.requestFocus()
            }
            false
        }

        mobileLayout.countryPicker.setOnCountryChangeListener {
            countryCode = PLUS + mobileLayout.countryPicker.selectedCountryCode
        }

        /** get email text from user */
        mobileLayout.etPhone.doAfterTextChanged {
            etPhone = it.toString()
            enableVerifyButton(mobileLayout, it.toString())
        }

        mobileLayout.otp1.doAfterTextChanged {
            otoFirst = it.toString()
        }

        mobileLayout.otp2.doAfterTextChanged {
            otpSec = it.toString()
        }

        mobileLayout.otp3.doAfterTextChanged {
            otpThird = it.toString()
        }

        mobileLayout.otp4.doAfterTextChanged {
            otpFourth = it.toString()
        }

        /** send otp on button verify for mobile login */
        mobileLayout.tvVerify.setOnClickListener {
            if (countryCode.isNotEmpty() || etPhone.isNotEmpty()) {
                viewModel.sendOtpLogin(countryCode, etPhone)
            } else {
                SnackBarUtil.showErrorSnackBar(
                    binding.root,
                    getString(R.string.please_enter_mobile)
                )
            }
        }


        val otpNumber = otoFirst + otpSec + otpThird + otpFourth
        if (otpNumber.length >= 4) {

        }

        //login with email
        // viewModel.emailUserLogin("raj@gmail.com","Raj@1234")

        ///validation
        // viewModel.isAllFieldValid(etEmail,etPassword)

        //send otp for mobile login
        //viewModel.sendOtpLogin("+91","9644756516")

        //login by mobile number
        //viewModel.loginWithMobileNumber("+91", "9644756516", "4321")

        //SnackBarUtil.showSuccessSnackBar(binding.root,getString(R.string.choose_an_option_below))
        // SnackBarUtil.showErrorSnackBar(binding.root,getString(R.string.choose_an_option_below))

        binding.emailLoginLayout.btnSignIn.setOnClickListener {
            viewModel.isAllFieldValid(etEmail, etPassword)
        }

        mobileLayout.btnSignInMobile.setOnClickListener {
            if (countryCode.isNotEmpty() && etPhone.isNotEmpty()) {
                makePhoneLoginApiCall()
            }
        }

    }

    private fun enableVerifyButton(mobileLayout: LoginMobileNumberBinding, phoneNumber: String) {
        if (countryCode.isEmpty() || phoneNumber.isEmpty()) {
            mobileLayout.tvVerify.visibility = View.GONE
            mobileLayout.tvVerifyDisable.visibility = View.VISIBLE
        } else {
            mobileLayout.tvVerify.visibility = View.VISIBLE
            mobileLayout.tvVerifyDisable.visibility = View.GONE
        }
    }

    private fun makePhoneLoginApiCall() {

    }

    private fun setUIStateObserver() {

        /** observe login validation*/
        viewModel.getEmailLoginValidation.observe(viewLifecycleOwner) {
            when (it) {
                is ValidationEmailLoginUIState.IsEmailEmpty -> {
                    SnackBarUtil.showErrorSnackBar(
                        binding.root,
                        getString(R.string.please_enter_email)
                    )
                }

                is ValidationEmailLoginUIState.EmailIsNotValid -> {
                    SnackBarUtil.showErrorSnackBar(
                        binding.root,
                        getString(R.string.please_enter_valid_email)
                    )
                }

                is ValidationEmailLoginUIState.IsPasswordEmpty -> {
                    SnackBarUtil.showErrorSnackBar(
                        binding.root,
                        getString(R.string.please_enter_password)
                    )
                }

                is ValidationEmailLoginUIState.OnAllDataValid -> {
                    makeEmailLoginApiCall()
                }
            }
        }

        /** observe login API response*/
        viewModel.getEmailLoginResponse.observe(viewLifecycleOwner) {
            when (it) {
                is EmailLoginUIState.IsLoading -> {}

                is EmailLoginUIState.OnSuccess -> {
                    SnackBarUtil.showSuccessSnackBar(binding.root, it.result.message.toString())
                }

                is EmailLoginUIState.OnFailure -> {
                    SnackBarUtil.showErrorSnackBar(binding.root, it.onFailure)
                }
            }
        }

        /** observe send otp response for mobile*/
        viewModel.getResponseSendOtpMobile.observe(viewLifecycleOwner) {
            when (it) {
                is SendMobileOtpUIState.IsLoading -> {}

                is SendMobileOtpUIState.OnSuccess -> {
                    SnackBarUtil.showSuccessSnackBar(binding.root, it.onSuccess.message.toString())
                }

                is SendMobileOtpUIState.OnFailure -> {
                    SnackBarUtil.showErrorSnackBar(binding.root, it.onFailure)
                }
            }
        }
    }

    private fun makeEmailLoginApiCall() {
        viewModel.emailUserLogin(etEmail, etPassword)
    }
}