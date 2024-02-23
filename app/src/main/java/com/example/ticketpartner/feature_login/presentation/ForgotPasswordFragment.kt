package com.example.ticketpartner.feature_login.presentation

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
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
import com.example.ticketpartner.common.EMAIL_KEY
import com.example.ticketpartner.common.RESET_TOKEN
import com.example.ticketpartner.common.SnackBarUtil
import com.example.ticketpartner.databinding.FragmentForgotPasswordBinding
import com.example.ticketpartner.feature_login.domain.model.ForgotPassSendEmailUIState
import com.example.ticketpartner.feature_login.domain.model.ForgotPassVerifyEmailUIState
import com.example.ticketpartner.feature_signup.SignUpViewModel
import com.example.ticketpartner.utils.DialogProgressUtil
import com.example.ticketpartner.utils.NavigateFragmentUtil.clearBackStackToDestination
import dagger.hilt.android.AndroidEntryPoint
import kotlin.math.log

@AndroidEntryPoint
class ForgotPasswordFragment : Fragment() {
    private lateinit var binding: FragmentForgotPasswordBinding
    private val viewModel: SignUpViewModel by activityViewModels()
    private var etEmail: String = ""
    private var token: String = ""

    private var otoFirst: String = ""
    private var otpSec: String = ""
    private var otpThird: String = ""
    private var otpFourth: String = ""
    private var isEmailVerify = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentForgotPasswordBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()
    }

    private fun initView() {

        binding.etEmail.doAfterTextChanged {
            etEmail = it.toString()
            if (it.toString().isEmpty()) {
                binding.tvVerify.visibility = View.GONE
                binding.tvVerifyDisable.visibility = View.VISIBLE
            } else {
                binding.tvVerify.visibility = View.VISIBLE
                binding.tvVerifyDisable.visibility = View.GONE
            }
        }

        binding.ivBack.setOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }

        binding.tvSignBtn.setOnClickListener {
            findNavController().clearBackStackToDestination(R.id.signInFragment)
        }

        binding.tvVerify.setOnClickListener {
            if (!ContactUsInputFieldValidator.isEmailValidPattern(etEmail)) {
                SnackBarUtil.showErrorSnackBar(
                    binding.root, getString(R.string.please_enter_valid_email)
                )
            } else {
                viewModel.sendEmailForgotPassword(etEmail)
                observeSendEmailResponse()
            }
        }

        /** get phone number from user */
        binding.includeOtpEmail.apply {
            otp1.addTextChangedListener(createTextWatcherEmail())
            otp2.addTextChangedListener(createTextWatcherEmail())
            otp3.addTextChangedListener(createTextWatcherEmail())
            otp4.addTextChangedListener(createTextWatcherEmail())
        }

        binding.btnContinue.setOnClickListener {
            val bundle = Bundle()
            bundle.putString(RESET_TOKEN, token)
            findNavController().navigate(R.id.resetPasswordFragment, bundle)
        }
    }

    private fun createTextWatcherEmail(): TextWatcher {
        return object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                // Check the total length of all four OTP fields
                val emailOtp = binding.includeOtpEmail
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

    private fun makeEmailOtpVerifyCall(combinedOTP: String) {
        if (combinedOTP.length == 4) {
            viewModel.verifyEmailForgotPass(etEmail, combinedOTP)

            viewModel.getForgotPassVerifyEmailResponse.observe(viewLifecycleOwner) {
                when (it) {
                    is ForgotPassVerifyEmailUIState.IsLoading -> {
                        DialogProgressUtil.show(childFragmentManager)
                    }

                    is ForgotPassVerifyEmailUIState.OnSuccess -> {
                         token = it.result.data?.reset_token.toString()
                        isEmailVerify = true
                        enableCreateAccountButton()
                        DialogProgressUtil.dismiss()
                        SnackBarUtil.showSuccessSnackBar(binding.root, it.result.message.toString())
                        binding.otpLayoutEmail.visibility = View.GONE
                        makeNonEditableEditText(binding.etEmail)
                        binding.tvVerify.visibility = View.GONE
                        binding.ivEmailVerify.visibility = View.VISIBLE

                    }

                    is ForgotPassVerifyEmailUIState.OnFailure -> {
                        isEmailVerify = false
                        enableCreateAccountButton()
                        DialogProgressUtil.dismiss()
                        SnackBarUtil.showErrorSnackBar(binding.root, it.onFailure)
                    }
                }
            }
        }
    }
    private fun makeNonEditableEditText(view: AppCompatEditText) {
        view.isEnabled = false
        view.isFocusable = false
        view.isCursorVisible = false
    }

    private fun enableCreateAccountButton() {
        if (isEmailVerify) {
            binding.btnContinue.visibility = View.VISIBLE
            binding.btnContinueDisable.visibility = View.GONE
        } else {
            binding.btnContinue.visibility = View.GONE
            binding.btnContinueDisable.visibility = View.VISIBLE
        }
    }

    private fun observeSendEmailResponse() {
        viewModel.getForgotPasswordSendEmailResponse.observe(viewLifecycleOwner) {
            when (it) {
                is ForgotPassSendEmailUIState.IsLoading -> {
                    DialogProgressUtil.show(childFragmentManager)
                }

                is ForgotPassSendEmailUIState.OnSuccess -> {
                    DialogProgressUtil.dismiss()
                    SnackBarUtil.showSuccessSnackBar(binding.root, it.result.message.toString())
                    binding.otpLayoutEmail.visibility = View.VISIBLE
                }

                is ForgotPassSendEmailUIState.OnFailure -> {
                    DialogProgressUtil.dismiss()
                    SnackBarUtil.showErrorSnackBar(binding.root, it.onFailure)
                }
            }
        }
    }
}