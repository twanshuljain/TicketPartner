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

        /** Switch email/phone layout based on radio button click */
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

        /** Redirect on sign-Up page */
        binding.tvSignUp.setOnClickListener {
            findNavController().navigate(R.id.signUpEmailFragment)
        }

        /** Redirect on forgot page */
        binding.emailLoginLayout.tvForgotPassword.setOnClickListener {
            findNavController().navigate(R.id.forgotPasswordFragment)
        }
    }


    private fun initView() {
        /** initialize countDown with 2 min. */
        countdownTimerUtil = CountdownTimerUtil(
            binding.phoneLoginLayout.tvCountDownTime,
            totalTimeMillis = 2 * 60 * 1000,  // 2 minutes
            intervalMillis = 1000,
            callback = this
        )

        val mobileLayout = binding.phoneLoginLayout

        /** get email text from user */
        binding.emailLoginLayout.etEmail.doAfterTextChanged {
            etEmail = it.toString().trim()
        }

        /** get password text from user */
        binding.emailLoginLayout.etPassword.doAfterTextChanged {
            etPassword = it.toString().trim()
        }

        /** get country code from country picker */
        mobileLayout.countryPicker.setOnCountryChangeListener {
            countryCode = PLUS + mobileLayout.countryPicker.selectedCountryCode
        }

        /** get phone number from user */
        mobileLayout.etPhone.doAfterTextChanged {
            etPhone = it.toString().trim()
            enableVerifyButton(mobileLayout, it.toString())
        }

        mobileLayout.otp1.doAfterTextChanged {
            otoFirst = it.toString().trim()
            if (otoFirst.isNotEmpty()) {
                mobileLayout.otp2.requestFocus()
            }

        }

        mobileLayout.otp2.doAfterTextChanged {
            otpSec = it.toString().trim()
            if (otpSec.isNotEmpty()) {
                mobileLayout.otp3.requestFocus()
            }

        }

        mobileLayout.otp3.doAfterTextChanged {
            otpThird = it.toString().trim()
            if (otpThird.isNotEmpty()) {
                mobileLayout.otp4.requestFocus()
            }
        }

        mobileLayout.otp4.doAfterTextChanged {
            otpFourth = it.toString().trim()
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

        /** resend otp button */
        mobileLayout.tvResendBtn.setOnClickListener {
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

        /** email login button */
        binding.emailLoginLayout.btnSignIn.setOnClickListener {
            val isValid = checkValidationForEmailLogin(etEmail, etPassword)
            if (isValid) {
                makeEmailLoginApiCall()
                observeEmailLoginResponse()
            }
        }

        /** make phone login API call on sign-in button click */
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

    /** validation for email login API */
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

    /** observe send otp response for mobile*/
    private fun observeSendOtoMobileResponse() {
        viewModel.getResponseSendOtpMobile.observe(viewLifecycleOwner) {
            when (it) {
                is SendMobileOtpUIState.IsLoading -> {
                    DialogProgressUtil.show(childFragmentManager)
                }

                is SendMobileOtpUIState.OnSuccess -> {
                    DialogProgressUtil.dismiss()
                    startCountDown()
                    binding.phoneLoginLayout.tvResendLayout.visibility = View.GONE
                    binding.phoneLoginLayout.tvCountDown.visibility = View.VISIBLE
                    binding.phoneLoginLayout.layoutOtpCount.visibility = View.VISIBLE
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

    /** Phone number login API calling */
    private fun makePhoneLoginApiCall(countryCode: String, otp: String, phoneNumber: String) {
        viewModel.loginWithMobileNumber(countryCode, phoneNumber, otp)
        observeMobileLoginResponse()
    }

    /** observe login with mobile API response */
    private fun observeMobileLoginResponse() {
        viewModel.getMobileLoginResponse.observe(viewLifecycleOwner) {
            when (it) {
                is MobileLoginUIState.IsLoading -> {
                    DialogProgressUtil.show(childFragmentManager)
                }

                is MobileLoginUIState.OnSuccess -> {
                    DialogProgressUtil.dismiss()
                    invalidOtpEditText(false)
                    SnackBarUtil.showSuccessSnackBar(binding.root, it.onSuccess.message.toString())
                }

                is MobileLoginUIState.OnFailure -> {
                    invalidOtpEditText(true)
                    DialogProgressUtil.dismiss()
                    SnackBarUtil.showErrorSnackBar(binding.root, it.onFailure)
                }
            }
        }
    }


    /** observe Email login API response*/
    private fun observeEmailLoginResponse() {
        viewModel.getEmailLoginResponse.observe(viewLifecycleOwner) {
            when (it) {
                is EmailLoginUIState.IsLoading -> {
                    DialogProgressUtil.show(childFragmentManager)
                }

                is EmailLoginUIState.OnSuccess -> {
                    DialogProgressUtil.dismiss()
                    SnackBarUtil.showSuccessSnackBar(binding.root, it.result.message.toString())

                    findNavController().navigate(R.id.addOrganizationChangeLogoFragment)
                }

                is EmailLoginUIState.OnFailure -> {
                    DialogProgressUtil.dismiss()
                    SnackBarUtil.showErrorSnackBar(binding.root, it.onFailure)
                }
            }
        }
    }

    /** Email login API calling */
    private fun makeEmailLoginApiCall() {
        viewModel.emailUserLogin(etEmail, etPassword)
    }

    /** show edit text in red color while invalid otp */
    private fun invalidOtpEditText(value: Boolean) {
        if (value) {
            binding.phoneLoginLayout.apply {
                otp1.setBackgroundResource(R.drawable.invalid_otp_design)
                otp2.setBackgroundResource(R.drawable.invalid_otp_design)
                otp3.setBackgroundResource(R.drawable.invalid_otp_design)
                otp4.setBackgroundResource(R.drawable.invalid_otp_design)
            }
        } else {
            binding.phoneLoginLayout.apply {
                otp1.setBackgroundResource(R.drawable.edit_text_design)
                otp2.setBackgroundResource(R.drawable.edit_text_design)
                otp3.setBackgroundResource(R.drawable.edit_text_design)
                otp4.setBackgroundResource(R.drawable.edit_text_design)
            }
        }
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

    /** start countDown through call this function */
    private fun startCountDown() {
        countdownTimerUtil.stop()
        disableSendOtpButton(false)
        countdownTimerUtil.start()
    }

    /** if you want condition on every count */
    override fun onTick(minutes: Long, seconds: Long) {}

    /** add condition on counter finish */
    override fun onFinish() {
        disableSendOtpButton(true)
        binding.phoneLoginLayout.tvResendLayout.visibility = View.VISIBLE
        binding.phoneLoginLayout.layoutOtpCount.visibility = View.GONE
        binding.phoneLoginLayout.tvCountDown.visibility = View.GONE
    }

    /** stop countDown on destroy current activity/fragment */
    override fun onDestroy() {
        super.onDestroy()
        countdownTimerUtil.stop()
    }
}