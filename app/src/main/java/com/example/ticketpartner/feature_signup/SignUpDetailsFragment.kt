package com.example.ticketpartner.feature_signup

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.ticketpartner.R
import com.example.ticketpartner.common.ContactUsInputFieldValidator
import com.example.ticketpartner.common.DELAY_TWO_SEC
import com.example.ticketpartner.common.EMAIL_KEY
import com.example.ticketpartner.common.IndianCountryCode
import com.example.ticketpartner.common.PLUS
import com.example.ticketpartner.common.SnackBarUtil
import com.example.ticketpartner.databinding.FragmentSignUpDetailsBinding
import com.example.ticketpartner.feature_login.domain.model.CreateUserAccountRequest
import com.example.ticketpartner.feature_login.domain.model.CreateUserAccountUIState
import com.example.ticketpartner.feature_login.domain.model.SendEmailOtpSignUpUIState
import com.example.ticketpartner.feature_login.domain.model.SendEmailOtpVerifyUIState
import com.example.ticketpartner.feature_login.domain.model.SendPhoneOtpSignUpUIState
import com.example.ticketpartner.feature_login.domain.model.SendPhoneOtpVerifyUIState
import com.example.ticketpartner.utils.CountdownTimerCallback
import com.example.ticketpartner.utils.CountdownTimerUtil
import com.example.ticketpartner.utils.DialogProgressUtil
import com.example.ticketpartner.utils.FullScreenDialogFragment
import com.example.ticketpartner.utils.NavigateFragmentUtil.clearBackStackToDestination
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SignUpDetailsFragment : Fragment(), CountdownTimerCallback {
    private lateinit var binding: FragmentSignUpDetailsBinding
    private val viewModel: SignUpViewModel by activityViewModels()
    private lateinit var countdownTimerUtil: CountdownTimerUtil
    private var isEmailVerify = false
    private var isPhoneVerify = false
    private var clickedOnEmailButton = false
    private var clickedOnPhoneButton = false

    private var etEmail: String = ""
    private var etPassword: String = ""
    private var etPhone: String = ""
    private var etFirstName: String = ""
    private var etLastName: String = ""
    private var countryCode: String = IndianCountryCode
    private var args = ""

    private var otoFirst: String = ""
    private var otpSec: String = ""
    private var otpThird: String = ""
    private var otpFourth: String = ""
    private var otpNumber: String = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentSignUpDetailsBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()
        arguments?.let {
            args = it.getString(EMAIL_KEY, "")
            binding.etEmail.setText(args)
        }
    }


    private fun initView() {
        binding.countryPicker.setOnCountryChangeListener {
            countryCode = PLUS + binding.countryPicker.selectedCountryCode
        }

        /** get email text from user */
        binding.etEmail.doAfterTextChanged {
            etEmail = it.toString().trim()
            disableSendOtpButtonEmail(it.toString())
        }

        binding.etPhoneNumber.doAfterTextChanged {
            etPhone = it.toString().trim()
            enableSendOtpButtonPhone(it.toString())
        }


        val accessOtp = binding.emailOtp
        accessOtp.apply {
            otp1.doAfterTextChanged {
                if (otoFirst.isNotEmpty()) {
                    accessOtp.otp2.requestFocus()
                }
                otp2.doAfterTextChanged {
                    if (otpSec.isNotEmpty()) {
                        accessOtp.otp3.requestFocus()
                    }
                }
                otp3.doAfterTextChanged {
                    if (otpThird.isNotEmpty()) {
                        accessOtp.otp4.requestFocus()
                    }
                }
            }
        }

        binding.emailOtp.tvResendBtn.setOnClickListener {
            clickedOnEmailButton = true
            clickedOnPhoneButton = false
            initialiseEmailCounter()
            sendEmailOtp()
        }

        binding.tvVerify.setOnClickListener {
            initialiseEmailCounter()
            sendEmailOtp()
        }

        binding.includeOtpPhone.tvResendBtn.setOnClickListener {
            clickedOnEmailButton = false
            clickedOnPhoneButton = true
            intialiseMobileOtpCounter()
            if (etPhone.isNotEmpty()) {
                viewModel.sendPhoneOtpSignUp(countryCode, etPhone)
                observeSendPhoneOtpResponse()
            }
        }

        binding.tvVerifyMobile.setOnClickListener {
            intialiseMobileOtpCounter()
            if (etPhone.isNotEmpty()) {
                viewModel.sendPhoneOtpSignUp(countryCode, etPhone)
                observeSendPhoneOtpResponse()
            }
        }

        binding.tvSignInText.setOnClickListener {
            findNavController().clearBackStackToDestination(R.id.signInFragment)
        }

        binding.emailOtp.apply {
            otp1.addTextChangedListener(createTextWatcherEmail())
            otp2.addTextChangedListener(createTextWatcherEmail())
            otp3.addTextChangedListener(createTextWatcherEmail())
            otp4.addTextChangedListener(createTextWatcherEmail())
        }

        binding.includeOtpPhone.apply {
            otp1.addTextChangedListener(createTextWatcherPhone())
            otp2.addTextChangedListener(createTextWatcherPhone())
            otp3.addTextChangedListener(createTextWatcherPhone())
            otp4.addTextChangedListener(createTextWatcherPhone())
        }

        binding.etFirstName.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                etFirstName = s.toString().trim()
                enableCreateAccountButton()
            }
        })

        binding.etLastName.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                etLastName = s.toString().trim()
                enableCreateAccountButton()
            }
        })

        binding.etPassword.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                etPassword = s.toString().trim()
                enableCreateAccountButton()
            }
        })


        binding.btnCreateAccount.setOnClickListener {
            viewModel.createUserAccount(
                CreateUserAccountRequest(
                    country_code = countryCode,
                    email = etEmail,
                    first_name = etFirstName,
                    last_name = etLastName,
                    mobile_number = etPhone,
                    password = etPassword
                )
            )
            observeCreateUserAccountResponse()
        }
    }

    private fun intialiseMobileOtpCounter() {
        countdownTimerUtil = CountdownTimerUtil(
            binding.includeOtpPhone.tvCountDownTime,
            totalTimeMillis = 2 * 60 * 1000,  // 2 minutes
            intervalMillis = 1000,
            callback = this
        )
    }

    private fun initialiseEmailCounter() {
        countdownTimerUtil = CountdownTimerUtil(
            binding.emailOtp.tvCountDownTime,
            totalTimeMillis = 2 * 60 * 1000,  // 2 minutes
            intervalMillis = 1000,
            callback = this
        )
    }

    private fun sendEmailOtp() {
        if (etEmail.isNotEmpty()) {
            if (!ContactUsInputFieldValidator.isEmailValidPattern(etEmail)) {
                SnackBarUtil.showErrorSnackBar(
                    binding.root, getString(R.string.please_enter_valid_email)
                )
            } else {
                viewModel.sendEmailOtp(etEmail)
                observeSendEmailOtpResponse()
            }
        } else {
            SnackBarUtil.showErrorSnackBar(
                binding.root, getString(R.string.please_enter_email)
            )
        }
    }

    private fun observeCreateUserAccountResponse() {
        viewModel.getCreateAccountResponse.observe(viewLifecycleOwner) {
            when (it) {
                is CreateUserAccountUIState.IsLoading -> {
                    DialogProgressUtil.show(childFragmentManager)
                }

                is CreateUserAccountUIState.OnSuccess -> {
                    DialogProgressUtil.dismiss()
                    SnackBarUtil.showSuccessSnackBar(binding.root, it.result.message.toString())
                    val signIn = R.id.signInFragment
                    findNavController().clearBackStackToDestination(signIn)
                    // Adding a delay of 2000 milliseconds (2 seconds)
                    Handler(Looper.getMainLooper()).postDelayed({
                        binding.btnCreateAccount.isClickable = false
                    }, DELAY_TWO_SEC)
                    binding.btnCreateAccount.isClickable = true
                }

                is CreateUserAccountUIState.OnFailure -> {
                    DialogProgressUtil.dismiss()
                    SnackBarUtil.showErrorSnackBar(binding.root, it.onFailure)
                }
            }
        }
    }

    private fun navigateToAnotherScreen() {
        val dialog = FullScreenDialogFragment.newInstance(
            "",
            getString(R.string.account_has_been_created_successfully)
        )
        dialog.onStart()
        val signIn = R.id.signInFragment
        if (dialog.hasDialogDismissed()) {
            findNavController().clearBackStackToDestination(signIn)
        }
    }

    private fun enableCreateAccountButton() {
        if (isEmailVerify && isPhoneVerify && etFirstName.isNotEmpty() && etLastName.isNotEmpty() && etPassword.isNotEmpty()) {
            binding.btnCreateAccount.visibility = View.VISIBLE
            binding.btnCreateAccountDisable.visibility = View.GONE
        } else {
            binding.btnCreateAccount.visibility = View.GONE
            binding.btnCreateAccountDisable.visibility = View.VISIBLE
        }
    }


    private fun disablePhoneSendOtpButton(value: Boolean) {
        if (value) {
            binding.tvVerifyMobile.visibility = View.VISIBLE
            binding.tvVerifyDisableMobile.visibility = View.GONE
        } else {
            binding.tvVerifyMobile.visibility = View.GONE
            binding.tvVerifyDisableMobile.visibility = View.VISIBLE
        }
    }

    private fun enableSendOtpButtonPhone(phone: String) {
        if (phone.isEmpty()) disablePhoneSendOtpButton(false) else disablePhoneSendOtpButton(true)
    }

    private fun createTextWatcherEmail(): TextWatcher {
        return object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // Not needed in this case
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                // Not needed in this case
            }

            override fun afterTextChanged(s: Editable?) {
                // Check the total length of all four OTP fields
                val emailOtp = binding.emailOtp
                val totalLength =
                    emailOtp.otp1.toString().length + emailOtp.otp2.toString().length + emailOtp.otp3.toString().length + emailOtp.otp4.toString().length

                // Trigger the API call when the total length is greater than 4
                if (totalLength > 4) {
                    val combinedOTP =
                        "${emailOtp.otp1.text}${emailOtp.otp2.text}${emailOtp.otp3.text}${emailOtp.otp4.text}"
                    makeEmailOtpVerifyCall(combinedOTP)
                }
            }
        }
    }

    private fun createTextWatcherPhone(): TextWatcher {
        return object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // Not needed in this case
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                // Not needed in this case
            }

            override fun afterTextChanged(s: Editable?) {
                // Check the total length of all four OTP fields
                val phoneOtp = binding.includeOtpPhone
                val totalLength =
                    phoneOtp.otp1.toString().length + phoneOtp.otp2.toString().length + phoneOtp.otp3.toString().length + phoneOtp.otp4.toString().length

                // Trigger the API call when the total length is greater than 4
                if (totalLength > 4) {
                    val combinedPhoneOtp =
                        "${phoneOtp.otp1.text}${phoneOtp.otp2.text}${phoneOtp.otp3.text}${phoneOtp.otp4.text}"
                    makePhoneOtpVerifyCall(combinedPhoneOtp)
                }
            }
        }
    }

    private fun makePhoneOtpVerifyCall(combinedPhoneOtp: String) {
        if (combinedPhoneOtp.length == 4) {
            viewModel.verifyPhoneSendOtp(countryCode, etPhone, combinedPhoneOtp)
            observeSendPhoneOtpVerifyResponse()
        }
    }

    private fun observeSendPhoneOtpVerifyResponse() {
        viewModel.getSendPhoneOtpVerify.observe(viewLifecycleOwner) {
            when (it) {
                is SendPhoneOtpVerifyUIState.IsLoading -> {
                    DialogProgressUtil.show(childFragmentManager)
                }

                is SendPhoneOtpVerifyUIState.OnSuccess -> {
                    DialogProgressUtil.dismiss()
                    invalidOtpEditTextPhone(false)
                    SnackBarUtil.showSuccessSnackBar(binding.root, it.result.message.toString())
                    makeNonEditableEditText(binding.etPhoneNumber)
                    binding.tvVerifyMobile.visibility = View.GONE
                    binding.tvVerifyDisableMobile.visibility = View.GONE
                    binding.ivMobileVerified.visibility = View.VISIBLE
                    binding.otpLayoutPhone.visibility = View.GONE
                    isPhoneVerify = true
                    enableCreateAccountButton()
                }

                is SendPhoneOtpVerifyUIState.OnFailure -> {
                    isPhoneVerify = false
                    invalidOtpEditTextPhone(true)
                    DialogProgressUtil.dismiss()
                    SnackBarUtil.showErrorSnackBar(binding.root, it.onFailure)
                }
            }
        }
    }

    private fun makeNonEditableEditText(view: AppCompatEditText) {
        view.isEnabled = false
        view.isFocusable = false
        view.isCursorVisible = false
    }

    private fun makeEmailOtpVerifyCall(combinedOTP: String) {
        if (combinedOTP.length == 4) {
            viewModel.verifyEmailSendOtp(etEmail, combinedOTP)
            observeSendEmailVerifyResponse()
        }
    }

    private fun observeSendEmailVerifyResponse() {
        viewModel.getSendEmailOtpVerify.observe(viewLifecycleOwner) {
            when (it) {
                is SendEmailOtpVerifyUIState.IsLoading -> {
                    DialogProgressUtil.show(childFragmentManager)
                }

                is SendEmailOtpVerifyUIState.OnSuccess -> {
                    DialogProgressUtil.dismiss()
                    SnackBarUtil.showSuccessSnackBar(binding.root, it.result.message.toString())
                    invalidOtpEditTextEmail(false)
                    makeNonEditableEditText(binding.etEmail)
                    binding.tvVerify.visibility = View.GONE
                    binding.tvVerifyDisable.visibility = View.GONE
                    binding.ivEmailVerify.visibility = View.VISIBLE
                    binding.emailOtpLayout.visibility = View.GONE
                    isEmailVerify = true
                    enableCreateAccountButton()
                }

                is SendEmailOtpVerifyUIState.OnFailure -> {
                    isEmailVerify = false
                    DialogProgressUtil.dismiss()
                    invalidOtpEditTextEmail(true)
                    SnackBarUtil.showErrorSnackBar(binding.root, it.onFailure)
                }
            }
        }
    }

    private fun observeSendPhoneOtpResponse() {
        viewModel.getSendPhoneOtp.observe(viewLifecycleOwner) {
            when (it) {
                is SendPhoneOtpSignUpUIState.IsLoading -> {
                    DialogProgressUtil.show(childFragmentManager)
                }

                is SendPhoneOtpSignUpUIState.OnSuccess -> {
                    DialogProgressUtil.dismiss()
                    SnackBarUtil.showSuccessSnackBar(binding.root, it.result.message.toString())
                    binding.otpLayoutPhone.visibility = View.VISIBLE
                    clickedOnEmailButton = false
                    clickedOnPhoneButton = true
                    startCountDown()
                    binding.includeOtpPhone.tvResendLayout.visibility = View.GONE
                    binding.includeOtpPhone.layoutOtpCount.visibility = View.VISIBLE
                    binding.includeOtpPhone.tvCountDown.visibility = View.VISIBLE
                }

                is SendPhoneOtpSignUpUIState.OnFailure -> {
                    DialogProgressUtil.dismiss()
                    SnackBarUtil.showErrorSnackBar(binding.root, it.onFailure)
                }
            }
        }
    }


    private fun observeSendEmailOtpResponse() {
        viewModel.getSendEmailOtpSignUp.observe(viewLifecycleOwner) {
            when (it) {
                is SendEmailOtpSignUpUIState.IsLoading -> {
                    DialogProgressUtil.show(childFragmentManager)
                }

                is SendEmailOtpSignUpUIState.OnSuccess -> {
                    DialogProgressUtil.dismiss()
                    SnackBarUtil.showSuccessSnackBar(binding.root, it.result.message.toString())
                    binding.emailOtpLayout.visibility = View.VISIBLE
                    clickedOnEmailButton = true
                    clickedOnPhoneButton = false
                    startCountDown()
                    binding.emailOtp.tvResendLayout.visibility = View.GONE
                    binding.emailOtp.tvCountDown.visibility = View.VISIBLE
                    binding.emailOtp.layoutOtpCount.visibility = View.VISIBLE
                }

                is SendEmailOtpSignUpUIState.OnFailure -> {
                    DialogProgressUtil.dismiss()
                    SnackBarUtil.showErrorSnackBar(binding.root, it.onFailure)
                }
            }
        }
    }

    private fun disableSendOtpButtonEmail(etEmail: String) {
        if (etEmail.isEmpty()) disableEmailSendOtpButton(false) else disableEmailSendOtpButton(true)
    }

    /** false -> to show disable sendOtp button
     * true -> to enable send otp button */
    private fun disableEmailSendOtpButton(value: Boolean) {
        if (value) {
            binding.tvVerify.visibility = View.VISIBLE
            binding.tvVerifyDisable.visibility = View.GONE
        } else {
            binding.tvVerify.visibility = View.GONE
            binding.tvVerifyDisable.visibility = View.VISIBLE
        }
    }

    private fun checkValidationForEmailLogin(username: String, password: String): Boolean {
        val isAllValid =
            ContactUsInputFieldValidator.isEmailValidPattern(username) && password.isNotEmpty()

        if (username.isEmpty()) {
            SnackBarUtil.showErrorSnackBar(
                binding.root, getString(R.string.please_enter_email)
            )
            return false
        }
        if (!ContactUsInputFieldValidator.isEmailValidPattern(username)) {
            SnackBarUtil.showErrorSnackBar(
                binding.root, getString(R.string.please_enter_valid_email)
            )
            return false
        }
        if (password.isEmpty()) {
            SnackBarUtil.showErrorSnackBar(
                binding.root, getString(R.string.please_enter_password)
            )
            return false
        }
        return isAllValid
    }

    private fun disableSendOtpButton(value: Boolean) {
        if (value) {
            binding.tvVerify.visibility = View.VISIBLE
            binding.tvVerifyDisable.visibility = View.GONE
        } else {
            binding.tvVerify.visibility = View.GONE
            binding.tvVerifyDisable.visibility = View.VISIBLE
        }
    }

    private fun invalidOtpEditTextEmail(value: Boolean) {
        if (value) {
            binding.emailOtp.apply {
                otp1.setBackgroundResource(R.drawable.invalid_otp_design)
                otp2.setBackgroundResource(R.drawable.invalid_otp_design)
                otp3.setBackgroundResource(R.drawable.invalid_otp_design)
                otp4.setBackgroundResource(R.drawable.invalid_otp_design)
            }
        } else {
            binding.emailOtp.apply {
                otp1.setBackgroundResource(R.drawable.edit_text_design)
                otp2.setBackgroundResource(R.drawable.edit_text_design)
                otp3.setBackgroundResource(R.drawable.edit_text_design)
                otp4.setBackgroundResource(R.drawable.edit_text_design)
            }
        }
    }

    private fun invalidOtpEditTextPhone(value: Boolean) {
        if (value) {
            binding.includeOtpPhone.apply {
                otp1.setBackgroundResource(R.drawable.invalid_otp_design)
                otp2.setBackgroundResource(R.drawable.invalid_otp_design)
                otp3.setBackgroundResource(R.drawable.invalid_otp_design)
                otp4.setBackgroundResource(R.drawable.invalid_otp_design)
            }
        } else {
            binding.emailOtp.apply {
                otp1.setBackgroundResource(R.drawable.edit_text_design)
                otp2.setBackgroundResource(R.drawable.edit_text_design)
                otp3.setBackgroundResource(R.drawable.edit_text_design)
                otp4.setBackgroundResource(R.drawable.edit_text_design)
            }
        }
    }

    override fun onTick(minutes: Long, seconds: Long) {}

    private fun startCountDown() {
        countdownTimerUtil.stop()
        disableSendOtpButton(false)
        disablePhoneSendOtpButton(false)
        countdownTimerUtil.start()
    }

    override fun onFinish() {
        if (clickedOnEmailButton) {
            //email
            binding.emailOtp.tvResendLayout.visibility = View.VISIBLE
            binding.emailOtp.layoutOtpCount.visibility = View.GONE
            binding.emailOtp.tvCountDown.visibility = View.GONE
            disableSendOtpButton(true)
        }
        if (clickedOnPhoneButton) {
            //phone
            disablePhoneSendOtpButton(true)
            binding.includeOtpPhone.tvResendLayout.visibility = View.VISIBLE
            binding.includeOtpPhone.layoutOtpCount.visibility = View.GONE
            binding.includeOtpPhone.tvCountDown.visibility = View.GONE
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        countdownTimerUtil.stop()
    }
}