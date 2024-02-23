package com.example.ticketpartner.feature_login.presentation


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.ticketpartner.R
import com.example.ticketpartner.common.ContactUsInputFieldValidator
import com.example.ticketpartner.common.IndianCountryCode
import com.example.ticketpartner.common.PLUS
import com.example.ticketpartner.common.SnackBarUtil
import com.example.ticketpartner.databinding.FragmentSignInBinding
import com.example.ticketpartner.databinding.LoginMobileNumberBinding
import com.example.ticketpartner.feature_login.domain.model.EmailLoginUIState
import com.example.ticketpartner.feature_login.domain.model.MobileLoginUIState
import com.example.ticketpartner.feature_login.domain.model.SendMobileOtpUIState
import com.example.ticketpartner.feature_login.domain.model.ValidationEmailLoginUIState
import com.example.ticketpartner.utils.CountdownTimerCallback
import com.example.ticketpartner.utils.CountdownTimerUtil
import com.example.ticketpartner.utils.DialogProgressUtil
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SignInFragment : Fragment(), CountdownTimerCallback {
    private lateinit var binding: FragmentSignInBinding
    private val viewModel: LoginViewModel by activityViewModels()
    private lateinit var countdownTimerUtil: CountdownTimerUtil
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

        /** Show email/phone layout based on radio button click */
        binding.radioGroup.setOnCheckedChangeListener { _, checkedId ->
            if (checkedId != -1) {
                when (checkedId) {
                    R.id.radioEmail -> {
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
        countdownTimerUtil = CountdownTimerUtil(
            binding.phoneLoginLayout.tvCountDownTime,
            totalTimeMillis = 4 * 60 * 1000,  // 4 minutes
            intervalMillis = 1000,
            callback = this
        )
        val mobileLayout = binding.phoneLoginLayout

        /** get email text from user */
        binding.emailLoginLayout.etEmail.doAfterTextChanged {
            etEmail = it.toString()
        }

        /** get password text from user */
        binding.emailLoginLayout.etPassword.doAfterTextChanged {
            etPassword = it.toString()
        }


        mobileLayout.countryPicker.setOnCountryChangeListener {
            countryCode = PLUS + mobileLayout.countryPicker.selectedCountryCode
        }

        /** get phone number from user */
        mobileLayout.etPhone.doAfterTextChanged {
            etPhone = it.toString()
            enableVerifyButton(mobileLayout, it.toString())
        }

        mobileLayout.otp1.doAfterTextChanged {
            otoFirst = it.toString()
            if (otoFirst.isNotEmpty()) {
                mobileLayout.otp2.requestFocus()
            }

        }

        mobileLayout.otp2.doAfterTextChanged {
            otpSec = it.toString()
            if (otpSec.isNotEmpty()) {
                mobileLayout.otp3.requestFocus()
            }

        }

        mobileLayout.otp3.doAfterTextChanged {
            otpThird = it.toString()
            if (otpThird.isNotEmpty()) {
                mobileLayout.otp4.requestFocus()
            }
        }

        mobileLayout.otp4.doAfterTextChanged {
            otpFourth = it.toString()
        }

        /** send otp on button verify for mobile login */
        mobileLayout.tvVerify.setOnClickListener {
            if (countryCode.isNotEmpty() || etPhone.isNotEmpty()) {
                viewModel.sendOtpLogin(countryCode, etPhone)
                observeSendOtoMobileResponse()
            } else {
                SnackBarUtil.showErrorSnackBar(
                    binding.root,
                    getString(R.string.please_enter_mobile)
                )
            }
        }


        binding.emailLoginLayout.btnSignIn.setOnClickListener {
            val isValid = checkValidationForEmailLogin(etEmail, etPassword)
            if (isValid) {
                makeEmailLoginApiCall()
                observeEmailLoginResponse()
            }
        }

        mobileLayout.btnSignInMobile.setOnClickListener {
            if (countryCode.isNotEmpty() && etPhone.isNotEmpty()) {
                val otpNumber = otoFirst + otpSec + otpThird + otpFourth
                if (otpNumber.length >= 4) {
                    makePhoneLoginApiCall(countryCode, otpNumber, etPhone)
                } else {
                    SnackBarUtil.showErrorSnackBar(
                        binding.root,
                        getString(R.string.otp_is_required)
                    )
                }
            } else {
                SnackBarUtil.showErrorSnackBar(
                    binding.root,
                    getString(R.string.please_enter_mobile)
                )
            }
        }
    }
    private fun checkValidationForEmailLogin(username: String, password: String): Boolean {
        val isAllValid =
            ContactUsInputFieldValidator.isEmailValidPattern(username) && password.isNotEmpty()

        if (username.isEmpty()) {
            SnackBarUtil.showErrorSnackBar(
                binding.root,
                getString(R.string.please_enter_email)
            )
            return false
        }
        if (!ContactUsInputFieldValidator.isEmailValidPattern(username)) {
            SnackBarUtil.showErrorSnackBar(
                binding.root,
                getString(R.string.please_enter_valid_email)
            )
            return false
        }
        if (password.isEmpty()) {
            SnackBarUtil.showErrorSnackBar(
                binding.root,
                getString(R.string.please_enter_password)
            )
            return false
        }
        return isAllValid
    }

    private fun observeSendOtoMobileResponse() {
        /** observe send otp response for mobile*/
        viewModel.getResponseSendOtpMobile.observe(viewLifecycleOwner) {
            when (it) {
                is SendMobileOtpUIState.IsLoading -> {
                    DialogProgressUtil.show(childFragmentManager)
                }

                is SendMobileOtpUIState.OnSuccess -> {
                    DialogProgressUtil.dismiss()
                    startCountDown()
                    SnackBarUtil.showSuccessSnackBar(binding.root, it.onSuccess.message.toString())
                }

                is SendMobileOtpUIState.OnFailure -> {
                    DialogProgressUtil.dismiss()
                    SnackBarUtil.showErrorSnackBar(binding.root, it.onFailure)
                }
            }
        }
    }

    private fun enableVerifyButton(mobileLayout: LoginMobileNumberBinding, phoneNumber: String) {
        if (countryCode.isEmpty() || phoneNumber.isEmpty()) {
            disableSendOtpButton(false)
        } else {
            disableSendOtpButton(true)
        }
    }

    private fun makePhoneLoginApiCall(countryCode: String, otp: String, phoneNumber: String) {
        viewModel.loginWithMobileNumber(countryCode, phoneNumber, otp)
        observeMobileLoginResponse()
    }

    private fun observeMobileLoginResponse() {
        /** observe login with mobile API response */
        viewModel.getMobileLoginResponse.observe(viewLifecycleOwner) {
            when (it) {
                is MobileLoginUIState.IsLoading -> {
                    DialogProgressUtil.show(childFragmentManager)
                }
                is MobileLoginUIState.OnSuccess -> {
                    DialogProgressUtil.dismiss()
                    SnackBarUtil.showSuccessSnackBar(binding.root, it.onSuccess.message.toString())
                }

                is MobileLoginUIState.OnFailure -> {
                    DialogProgressUtil.dismiss()
                    SnackBarUtil.showErrorSnackBar(binding.root, it.onFailure)
                }
            }
        }
    }

    private fun setValidationStateObserver() {
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
                }
            }
        }
    }

    private fun observeEmailLoginResponse() {
        /** observe Email login API response*/
        viewModel.getEmailLoginResponse.observe(viewLifecycleOwner) {
            when (it) {
                is EmailLoginUIState.IsLoading -> {
                    DialogProgressUtil.show(childFragmentManager)
                }

                is EmailLoginUIState.OnSuccess -> {
                    DialogProgressUtil.dismiss()
                    SnackBarUtil.showSuccessSnackBar(binding.root, it.result.message.toString())
                   // viewModel.getEmailLoginResponse
                }

                is EmailLoginUIState.OnFailure -> {
                    DialogProgressUtil.dismiss()
                    SnackBarUtil.showErrorSnackBar(binding.root, it.onFailure)
                }
            }
        }
    }

    private fun makeEmailLoginApiCall() {
        viewModel.emailUserLogin(etEmail, etPassword)
    }


    /** false -> to show disable sendOtp button
     * true -> to enable send otp button */
    private fun disableSendOtpButton(value: Boolean) {
        if (value) {
            binding.phoneLoginLayout.tvVerify.visibility = View.VISIBLE
            binding.phoneLoginLayout.tvVerifyDisable.visibility = View.GONE
        } else {
            binding.phoneLoginLayout.tvVerify.visibility = View.GONE
            binding.phoneLoginLayout.tvVerifyDisable.visibility = View.VISIBLE
        }
    }

    private fun startCountDown() {
        disableSendOtpButton(false)

        countdownTimerUtil.start()
    }

    override fun onTick(minutes: Long, seconds: Long) {}

    override fun onFinish() {
        disableSendOtpButton(true)
    }

    override fun onDestroy() {
        super.onDestroy()
        countdownTimerUtil.stop()
    }
}