package com.example.ticketpartner.feature_login.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ticketpartner.common.ContactUsInputFieldValidator
import com.example.ticketpartner.common.LogUtil
import com.example.ticketpartner.common.storage.MyPreferences
import com.example.ticketpartner.common.storage.PrefConstants
import com.example.ticketpartner.feature_login.domain.model.EmailLoginUIState
import com.example.ticketpartner.feature_login.domain.model.ForgotPassSendEmailUIState
import com.example.ticketpartner.feature_login.domain.model.ForgotPassVerifyEmailUIState
import com.example.ticketpartner.feature_login.domain.model.ForgotPasswordSendEmailLinkUIState
import com.example.ticketpartner.feature_login.domain.model.MobileLoginUIState
import com.example.ticketpartner.feature_login.domain.model.ResetPasswordUIState
import com.example.ticketpartner.feature_login.domain.model.SendMobileOtpUIState
import com.example.ticketpartner.feature_login.domain.model.ValidationEmailLoginUIState
import com.example.ticketpartner.feature_login.domain.model.ValidationMobileLoginUIState
import com.example.ticketpartner.feature_login.domain.usecase.GetEmailLoginUseCase
import com.example.ticketpartner.feature_login.domain.usecase.GetForgotPassSendEmailUseCase
import com.example.ticketpartner.feature_login.domain.usecase.GetPhoneLoginUseCase
import com.example.ticketpartner.feature_login.domain.usecase.GetResetPasswordUseCase
import com.example.ticketpartner.feature_login.domain.usecase.GetSendEmailLinkUseCase
import com.example.ticketpartner.feature_login.domain.usecase.GetSendOtpLoginUseCase
import com.example.ticketpartner.feature_login.domain.usecase.GetVerifyEmailForgotPassUseCase
import com.technotoil.tglivescan.common.retrofit.apis.ErrorResponseHandler
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val getEmailLoginUseCase: GetEmailLoginUseCase,
    private val getSendOtpLoginUseCase: GetSendOtpLoginUseCase,
    private val getPhoneLoginUseCase: GetPhoneLoginUseCase,
    private val getSendEmailLinkUseCase: GetSendEmailLinkUseCase,
    private val getForgotPassSendEmailUseCase: GetForgotPassSendEmailUseCase,
    private val getVerifyEmailForgotPassUseCase: GetVerifyEmailForgotPassUseCase,
    private val getResetPasswordUseCase: GetResetPasswordUseCase,
    private val logUtil: LogUtil
) : ViewModel() {

    private val _emailValidationState: MutableLiveData<ValidationEmailLoginUIState> =
        MutableLiveData()
    val getEmailLoginValidation: LiveData<ValidationEmailLoginUIState> = _emailValidationState

    private val _mobileValidationState: MutableLiveData<ValidationMobileLoginUIState> =
        MutableLiveData()
    val getMobileLoginValidation: LiveData<ValidationMobileLoginUIState> = _mobileValidationState

    private val _emailLoginState: MutableLiveData<EmailLoginUIState> = MutableLiveData()
    val getEmailLoginResponse: LiveData<EmailLoginUIState> = _emailLoginState

    private val _sendMobileOtpState: MutableLiveData<SendMobileOtpUIState> = MutableLiveData()
    val getResponseSendOtpMobile: LiveData<SendMobileOtpUIState> = _sendMobileOtpState

    private val _mobileLoginState: MutableLiveData<MobileLoginUIState> = MutableLiveData()
    val getMobileLoginResponse: LiveData<MobileLoginUIState> = _mobileLoginState

    private val _sendEmailLinkState: MutableLiveData<ForgotPasswordSendEmailLinkUIState> =
        MutableLiveData()
    val getSendEmailLinkForgotPasswordResponse: LiveData<ForgotPasswordSendEmailLinkUIState> =
        _sendEmailLinkState

    private val _forgotPasswordSendEmail: MutableLiveData<ForgotPassSendEmailUIState> =
        MutableLiveData()
    val getForgotPasswordSendEmailResponse: LiveData<ForgotPassSendEmailUIState> =
        _forgotPasswordSendEmail

    private val _forgotPasswordVerifyEmail: MutableLiveData<ForgotPassVerifyEmailUIState> =
        MutableLiveData()
    val getForgotPassVerifyEmailResponse: LiveData<ForgotPassVerifyEmailUIState> =
        _forgotPasswordVerifyEmail

    private val _resetPassword: MutableLiveData<ResetPasswordUIState> = MutableLiveData()
    val getResetPassword: LiveData<ResetPasswordUIState> = _resetPassword

    private fun updateLoginState(newState: EmailLoginUIState) {
        _emailLoginState.value = newState
    }


    fun emailUserLogin(email: String, password: String) {
        _emailLoginState.value = EmailLoginUIState.IsLoading(isLoading = true)
        viewModelScope.launch {
            getEmailLoginUseCase.invoke(email, password).catch {
                logUtil.log(TAG, "onError${it.message.toString()}")
                val error = ErrorResponseHandler(it)
                _emailLoginState.value =
                    EmailLoginUIState.OnFailure(error.getErrors().message.toString())
            }.collect {
                logUtil.log(TAG, "onCollect${it}")
                it.data?.access_token?.let { accessToken ->
                    MyPreferences.putString(
                        PrefConstants.ACCESS_TOKEN, accessToken.toString()
                    )
                }
                _emailLoginState.value = EmailLoginUIState.OnSuccess(it)
            }
        }
    }

    fun sendOtpLogin(countryCode: String, number: String) {
        _sendMobileOtpState.value = SendMobileOtpUIState.IsLoading(true)
        viewModelScope.launch {
            getSendOtpLoginUseCase.invoke(countryCode, number).catch {
                val error = ErrorResponseHandler(it)
                _sendMobileOtpState.value =
                    SendMobileOtpUIState.OnFailure(error.getErrors().message.toString())
                logUtil.log(TAG, "onError: ${it.message}")
            }.collect {
                logUtil.log(TAG, "onResponse: $it")
                _sendMobileOtpState.value = SendMobileOtpUIState.OnSuccess(it)
            }
        }
    }

    fun loginWithMobileNumber(countryCode: String, number: String, otp: String) {
        _mobileLoginState.value = MobileLoginUIState.IsLoading(true)
        viewModelScope.launch {
            getPhoneLoginUseCase.invoke(countryCode, number, otp).catch {
                logUtil.log(TAG, "onError: ${it.message}")
                val error = ErrorResponseHandler(it)
                _mobileLoginState.value =
                    MobileLoginUIState.OnFailure(error.getErrors().message.toString())
            }.collect {
                logUtil.log(TAG, "onResponse: $it")
                it.data?.access_token?.let { accessToken ->
                    MyPreferences.putString(
                        PrefConstants.ACCESS_TOKEN, accessToken.toString()
                    )
                }
                _mobileLoginState.value = MobileLoginUIState.OnSuccess(it)
            }
        }
    }

    fun sendEmailLinkForgotPassword(email: String) {
        _sendEmailLinkState.value = ForgotPasswordSendEmailLinkUIState.IsLoading(true)
        viewModelScope.launch {
            getSendEmailLinkUseCase.invoke(email).catch {
                logUtil.log(TAG, "onError: ${it.message}")
                val error = ErrorResponseHandler(it)
                _sendEmailLinkState.value =
                    ForgotPasswordSendEmailLinkUIState.OnFailure(error.getErrors().message.toString())
            }.collect {
                logUtil.log(TAG, "onResponse: $it")
                _sendEmailLinkState.value = ForgotPasswordSendEmailLinkUIState.OnSuccess(it)
            }
        }
    }

    fun sendEmailForgotPassword(email: String) {
        _forgotPasswordSendEmail.value = ForgotPassSendEmailUIState.IsLoading(true)
        viewModelScope.launch {
            getForgotPassSendEmailUseCase.invoke(email).catch {
                logUtil.log(LoginViewModel.TAG, "onError${it.message.toString()}")
                val error = ErrorResponseHandler(it)
                _forgotPasswordSendEmail.value =
                    ForgotPassSendEmailUIState.OnFailure(error.getErrors().message.toString())
            }.collect {
                logUtil.log(LoginViewModel.TAG, "onCollect${it}")
                _forgotPasswordSendEmail.value = ForgotPassSendEmailUIState.OnSuccess(it)
            }
        }
    }

    fun verifyEmailForgotPass(email: String, otpNumber: String) {
        _forgotPasswordVerifyEmail.value = ForgotPassVerifyEmailUIState.IsLoading(true)
        viewModelScope.launch {
            getVerifyEmailForgotPassUseCase.invoke(email, otpNumber).catch {
                logUtil.log(LoginViewModel.TAG, "onError${it.message.toString()}")
                val error = ErrorResponseHandler(it)
                _forgotPasswordVerifyEmail.value =
                    ForgotPassVerifyEmailUIState.OnFailure(error.getErrors().message.toString())
            }.collect {
                logUtil.log(LoginViewModel.TAG, "onCollect${it}")
                /*  MyPreferences.putString(
                      PrefConstants.ACCESS_TOKEN, it.data?.reset_token.toString()
                  )*/
                _forgotPasswordVerifyEmail.value = ForgotPassVerifyEmailUIState.OnSuccess(it)
            }
        }
    }

    fun resetPassword(newPassword: String, conPassword: String) {
        _resetPassword.value = ResetPasswordUIState.IsLoading(true)
        viewModelScope.launch {
            getResetPasswordUseCase.invoke(newPassword, conPassword).catch {
                val error = ErrorResponseHandler(it)
                _resetPassword.value =
                    ResetPasswordUIState.OnFailure(error.getErrors().message.toString())
            }.collect {
                logUtil.log(LoginViewModel.TAG, "onCollect${it}")
                _resetPassword.value = ResetPasswordUIState.OnSuccess(it)
            }
        }
    }

    fun isUserNameValid(username: String) {
        if (!ContactUsInputFieldValidator.isEmailValidPattern(username)) {
            _emailValidationState.value =
                ValidationEmailLoginUIState.EmailIsNotValid("Email is not valaid")
        }
    }


    companion object {
        val TAG = LoginViewModel::class.java.simpleName
    }


}