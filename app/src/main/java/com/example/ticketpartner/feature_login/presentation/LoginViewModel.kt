package com.example.ticketpartner.feature_login.presentation

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ticketpartner.common.ContactUsInputFieldValidator
import com.example.ticketpartner.common.LogUtil
import com.example.ticketpartner.feature_login.domain.model.EmailLoginUIState
import com.example.ticketpartner.feature_login.domain.model.MobileLoginUIState
import com.example.ticketpartner.feature_login.domain.model.ValidationEmailLoginUIState
import com.example.ticketpartner.feature_login.domain.model.SendMobileOtpUIState
import com.example.ticketpartner.feature_login.domain.usecase.GetEmailLoginUseCase
import com.example.ticketpartner.feature_login.domain.usecase.GetPhoneLoginUseCase
import com.example.ticketpartner.feature_login.domain.usecase.GetSendOtpLoginUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val getEmailLoginUseCase: GetEmailLoginUseCase,
    private val getSendOtpLoginUseCase: GetSendOtpLoginUseCase,
    private val getPhoneLoginUseCase: GetPhoneLoginUseCase,
    private val logUtil: LogUtil
) : ViewModel() {

    private val _EmailLoginState: MutableLiveData<ValidationEmailLoginUIState> = MutableLiveData()
    val emailLoginUiState: LiveData<ValidationEmailLoginUIState> = _EmailLoginState

    private val _loginState: MutableLiveData<EmailLoginUIState> = MutableLiveData()
    val loginUiState: LiveData<EmailLoginUIState> = _loginState

    private val _sendMobileOtpState: MutableLiveData<SendMobileOtpUIState> = MutableLiveData()
    val verifyMobileOtpState: LiveData<SendMobileOtpUIState> = _sendMobileOtpState

    private val _mobileLoginState: MutableLiveData<MobileLoginUIState> = MutableLiveData()
    val mobileLoginState: LiveData<MobileLoginUIState> = _mobileLoginState


    /*
        fun isUserNameValid(username: String) {
            val result = ContactUsInputFieldValidator.isEmailValidPattern(username)
            updateLoginState(EmailLoginUiState.OnEmailEnter(result))
        }*/

    private fun updateLoginState(newState: EmailLoginUIState) {
        _loginState.value = newState
    }


    fun emailUserLogin(email: String, password: String) {
        _loginState.value = EmailLoginUIState.Loading(isLoading = true)
        viewModelScope.launch {
            getEmailLoginUseCase.invoke(email, password).catch {
                Log.e(TAG, "error: $it")
            }.onCompletion {
                logUtil.log(TAG, it?.message.toString())
            }.collect {
                Log.e(TAG, "emailUserLogin: $it")
            }
        }
    }

    fun sendOtpLogin(countryCode: String, number: String) {
        _sendMobileOtpState.value = SendMobileOtpUIState.OnLoading(true)
        viewModelScope.launch {
            getSendOtpLoginUseCase.invoke(countryCode, number).catch {
                _sendMobileOtpState.value =
                    SendMobileOtpUIState.OnFailure(it.message.toString())
                logUtil.log(TAG, "onError: ${it.message}")
            }.onCompletion {
                logUtil.log(TAG, "onCompletion ${it?.message.toString()}")
            }.collect {
                logUtil.log(TAG, "onResponse: $it")
                _sendMobileOtpState.value = SendMobileOtpUIState.OnSuccess(it)
            }
        }
    }

    fun loginWithMobileNumber(countryCode: String, number: String, otp: String) {
        _mobileLoginState.value = MobileLoginUIState.OnLoading(true)
        viewModelScope.launch {
            getPhoneLoginUseCase.invoke(countryCode, number, otp).onCompletion {
                logUtil.log(TAG, "onCompletion ${it?.message.toString()}")
            }.catch {
                logUtil.log(TAG, "onError: ${it.message}")
                _mobileLoginState.value =
                    MobileLoginUIState.OnFailure(it.message.toString())
            }.collect {
                logUtil.log(TAG, "onResponse: $it")
                _mobileLoginState.value = MobileLoginUIState.OnSuccess(it)
            }
        }
    }

    fun isUserNameValid(username: String) {
        if (!ContactUsInputFieldValidator.isEmailValidPattern(username)) {
            _EmailLoginState.value =
                ValidationEmailLoginUIState.EmailIsNotValid("Email is not valaid")
        }
    }

    fun isAllFieldValid(username: String, password: String) {
        val isAllValid =
            ContactUsInputFieldValidator.isEmailValidPattern(username) && password.isNotEmpty()
        _EmailLoginState.value = ValidationEmailLoginUIState.Loading(true)

        if (username.isEmpty() || password.isEmpty()) {
            _EmailLoginState.value =
                ValidationEmailLoginUIState.AllFieldsRequired("All fields required")
            // _EmailLoginState.value =  ValidationEmailLoginUIState.IsPasswordEmpty("Password is empty")
            return
        }
        if (!ContactUsInputFieldValidator.isEmailValidPattern(username)) {
            _EmailLoginState.value =
                ValidationEmailLoginUIState.EmailIsNotValid("Email is not valaid")
            return
        }
        if (isAllValid) {
            _EmailLoginState.value = ValidationEmailLoginUIState.OnAllDataValid(true)
        } else {
            _EmailLoginState.value =
                ValidationEmailLoginUIState.AllFieldsRequired("All fields required")
        }
    }

    companion object {
        val TAG = LoginViewModel::class.java.simpleName
    }


}