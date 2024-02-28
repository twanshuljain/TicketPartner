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
import com.example.ticketpartner.feature_login.domain.model.MobileLoginUIState
import com.example.ticketpartner.feature_login.domain.model.SendMobileOtpUIState
import com.example.ticketpartner.feature_login.domain.model.ValidationEmailLoginUIState
import com.example.ticketpartner.feature_login.domain.model.ValidationMobileLoginUIState
import com.example.ticketpartner.feature_login.domain.usecase.GetEmailLoginUseCase
import com.example.ticketpartner.feature_login.domain.usecase.GetPhoneLoginUseCase
import com.example.ticketpartner.feature_login.domain.usecase.GetSendOtpLoginUseCase
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


    /*
        fun isUserNameValid(username: String) {
            val result = ContactUsInputFieldValidator.isEmailValidPattern(username)
            updateLoginState(EmailLoginUiState.OnEmailEnter(result))
        }*/

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
                it.data?.access_token?.let {accessToken ->
                    MyPreferences.putString(
                        PrefConstants.ACCESS_TOKEN,accessToken.toString()
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
                _mobileLoginState.value = MobileLoginUIState.OnSuccess(it)
            }
        }
    }

    fun isUserNameValid(username: String) {
        if (!ContactUsInputFieldValidator.isEmailValidPattern(username)) {
            _emailValidationState.value =
                ValidationEmailLoginUIState.EmailIsNotValid("Email is not valaid")
        }
    }

    fun isAllFieldValid(username: String, password: String) {
        val isAllValid =
            ContactUsInputFieldValidator.isEmailValidPattern(username) && password.isNotEmpty()
        // _EmailLoginState.value = ValidationEmailLoginUIState.Loading(true)

        if (username.isEmpty()) {
            _emailValidationState.value = ValidationEmailLoginUIState.IsEmailEmpty(true)
            return
        }
        if (!ContactUsInputFieldValidator.isEmailValidPattern(username)) {
            _emailValidationState.value =
                ValidationEmailLoginUIState.EmailIsNotValid("Email is not valaid")
            return
        }
        if (password.isEmpty()) {
            _emailValidationState.value = ValidationEmailLoginUIState.IsPasswordEmpty(true)
            return
        }
        if (isAllValid) {
            _emailValidationState.value = ValidationEmailLoginUIState.OnAllDataValid(true)
            return
        }
    }

    fun isAllFieldValidMobile(countryCode: String, number: String) {
        val isAllValid = countryCode.isNotEmpty() && number.isNotEmpty()
        if (countryCode.isEmpty()) {
            _mobileValidationState.value = ValidationMobileLoginUIState.IsMobileEmpty(true)
            return
        }
        if (number.isEmpty()) {
            _mobileValidationState.value = ValidationMobileLoginUIState.IsMobileEmpty(true)
            return
        }
        if (isAllValid) {
            _mobileValidationState.value = ValidationMobileLoginUIState.OnAllDataValid(true)
        }
    }

    companion object {
        val TAG = LoginViewModel::class.java.simpleName
    }


}