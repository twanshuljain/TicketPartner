package com.example.ticketpartner.feature_signup

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ticketpartner.common.LogUtil
import com.example.ticketpartner.feature_login.domain.model.CreateUserAccountRequest
import com.example.ticketpartner.feature_login.domain.model.CreateUserAccountUIState
import com.example.ticketpartner.feature_login.domain.model.ForgotPassSendEmailResponse
import com.example.ticketpartner.feature_login.domain.model.ForgotPassSendEmailUIState
import com.example.ticketpartner.feature_login.domain.model.ForgotPassVerifyEmailUIState
import com.example.ticketpartner.feature_login.domain.usecase.GetSendOtpPhoneSignUpUseCase
import com.example.ticketpartner.feature_login.domain.model.SendEmailOtpSignUpUIState
import com.example.ticketpartner.feature_login.domain.model.SendEmailOtpVerifyUIState
import com.example.ticketpartner.feature_login.domain.model.SendPhoneOtpSignUpUIState
import com.example.ticketpartner.feature_login.domain.model.SendPhoneOtpVerifyUIState
import com.example.ticketpartner.feature_login.domain.usecase.GetSendOtpEmailSignUpUseCase
import com.example.ticketpartner.feature_login.domain.usecase.GetSendOtpEmailVerifyUseCase
import com.example.ticketpartner.feature_login.domain.usecase.GetSendOtpPhoneVerifySignUpUseCase
import com.example.ticketpartner.feature_login.domain.usecase.GetCreateUserAccountUseCase
import com.example.ticketpartner.feature_login.domain.usecase.GetForgotPassSendEmailUseCase
import com.example.ticketpartner.feature_login.domain.usecase.GetVerifyEmailForgotPassUseCase
import com.example.ticketpartner.feature_login.presentation.LoginViewModel
import com.technotoil.tglivescan.common.retrofit.apis.ErrorResponseHandler
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val getSendOtpEmailSignUpUseCase: GetSendOtpEmailSignUpUseCase,
    private val getSendOtpEmailVerifyUseCase: GetSendOtpEmailVerifyUseCase,
    private val getSendOtpPhoneSignUpUseCase: GetSendOtpPhoneSignUpUseCase,
    private val  getSendOtpPhoneVerifySignUpUseCase: GetSendOtpPhoneVerifySignUpUseCase,
    private val getCreateUserAccountUseCase: GetCreateUserAccountUseCase,
    private val getForgotPassSendEmailUseCase: GetForgotPassSendEmailUseCase,
    private val getVerifyEmailForgotPassUseCase: GetVerifyEmailForgotPassUseCase,
    private val logUtil: LogUtil
) : ViewModel() {

    private val _sendEmailOtpSignUp: MutableLiveData<SendEmailOtpSignUpUIState> = MutableLiveData()
    val getSendEmailOtpSignUp: LiveData<SendEmailOtpSignUpUIState> = _sendEmailOtpSignUp

    private val _sendEmailOtpVerify: MutableLiveData<SendEmailOtpVerifyUIState> = MutableLiveData()
    val getSendEmailOtpVerify: LiveData<SendEmailOtpVerifyUIState> = _sendEmailOtpVerify

    private val _sendPhoneOtp: MutableLiveData<SendPhoneOtpSignUpUIState> = MutableLiveData()
    val getSendPhoneOtp: LiveData<SendPhoneOtpSignUpUIState> = _sendPhoneOtp

    private val _sendPhoneOtpVerify: MutableLiveData<SendPhoneOtpVerifyUIState> = MutableLiveData()
    val getSendPhoneOtpVerify: LiveData<SendPhoneOtpVerifyUIState> = _sendPhoneOtpVerify

    private val _createAccountResponse: MutableLiveData<CreateUserAccountUIState> = MutableLiveData()
    val getCreateAccountResponse: LiveData<CreateUserAccountUIState> = _createAccountResponse

    private val _forgotPasswordSendEmail: MutableLiveData<ForgotPassSendEmailUIState> = MutableLiveData()
    val getForgotPasswordSendEmailResponse: LiveData<ForgotPassSendEmailUIState> = _forgotPasswordSendEmail

    private val _forgotPasswordVerifyEmail: MutableLiveData<ForgotPassVerifyEmailUIState> = MutableLiveData()
    val getForgotPassVerifyEmailResponse: LiveData<ForgotPassVerifyEmailUIState> = _forgotPasswordVerifyEmail



    fun sendEmailOtp(email: String) {
        _sendEmailOtpSignUp.value = SendEmailOtpSignUpUIState.IsLoading(true)
        viewModelScope.launch {
            getSendOtpEmailSignUpUseCase.invoke(email).catch {
                val error = ErrorResponseHandler(it)
                _sendEmailOtpSignUp.value =
                    SendEmailOtpSignUpUIState.OnFailure(error.getErrors().message.toString())
                logUtil.log(LoginViewModel.TAG, "onError${it.message.toString()}")
            }.collect {
                logUtil.log(LoginViewModel.TAG, "onCollect${it}")
                _sendEmailOtpSignUp.value = SendEmailOtpSignUpUIState.OnSuccess(it)
            }
        }
    }

    fun verifyEmailSendOtp(email: String, otpNumber: String) {
        _sendEmailOtpVerify.value = SendEmailOtpVerifyUIState.IsLoading(true)
        viewModelScope.launch {
            getSendOtpEmailVerifyUseCase.invoke(email, otpNumber).catch {
                val error = ErrorResponseHandler(it)
                _sendEmailOtpVerify.value =
                    SendEmailOtpVerifyUIState.OnFailure(error.getErrors().message.toString())
                logUtil.log(LoginViewModel.TAG, "onError${it.message.toString()}")
            }.collect {
                logUtil.log(LoginViewModel.TAG, "onCollect${it}")
                _sendEmailOtpVerify.value = SendEmailOtpVerifyUIState.OnSuccess(it)
            }
        }
    }

    fun sendPhoneOtpSignUp(countryCode: String, phoneNumber: String) {
        _sendPhoneOtp.value = SendPhoneOtpSignUpUIState.IsLoading(true)
        viewModelScope.launch {
            getSendOtpPhoneSignUpUseCase.invoke(countryCode,phoneNumber).catch {
                val error = ErrorResponseHandler(it)
                _sendPhoneOtp.value = SendPhoneOtpSignUpUIState.OnFailure(error.getErrors().message.toString())
                logUtil.log(LoginViewModel.TAG, "onError${it.message.toString()}")
            }.collect{
                _sendPhoneOtp.value = SendPhoneOtpSignUpUIState.OnSuccess(it)
                logUtil.log(LoginViewModel.TAG, "onCollect${it}")
            }
        }
    }

    fun verifyPhoneSendOtp(countryCode: String,phoneNumber: String,otpNumber: String){
        _sendPhoneOtpVerify.value = SendPhoneOtpVerifyUIState.IsLoading(true)
        viewModelScope.launch {
            getSendOtpPhoneVerifySignUpUseCase.invoke(countryCode,phoneNumber,otpNumber).catch {
                logUtil.log(LoginViewModel.TAG, "onError${it.message.toString()}")
                val error = ErrorResponseHandler(it)
                _sendPhoneOtpVerify.value = SendPhoneOtpVerifyUIState.OnFailure(error.getErrors().message.toString())
            }.collect{
                logUtil.log(LoginViewModel.TAG, "onCollect${it}")
                _sendPhoneOtpVerify.value = SendPhoneOtpVerifyUIState.OnSuccess(it)
            }
        }
    }

    fun createUserAccount(createUserAccountRequest: CreateUserAccountRequest){
        _createAccountResponse.value = CreateUserAccountUIState.IsLoading(true)
        viewModelScope.launch {
            getCreateUserAccountUseCase.invoke(createUserAccountRequest).catch {
                logUtil.log(LoginViewModel.TAG, "onError${it.message.toString()}")
                val error = ErrorResponseHandler(it)
                _createAccountResponse.value = CreateUserAccountUIState.OnFailure(error.getErrors().message.toString())
            }.collect{
                logUtil.log(LoginViewModel.TAG, "onCollect${it}")
                _createAccountResponse.value = CreateUserAccountUIState.OnSuccess(it)
            }
        }
    }

    fun sendEmailForgotPassword(email: String){
        _forgotPasswordSendEmail.value = ForgotPassSendEmailUIState.IsLoading(true)
        viewModelScope.launch {
            getForgotPassSendEmailUseCase.invoke(email).catch {
                logUtil.log(LoginViewModel.TAG, "onError${it.message.toString()}")
                val error = ErrorResponseHandler(it)
                _forgotPasswordSendEmail.value =ForgotPassSendEmailUIState.OnFailure(error.getErrors().message.toString())
            }.collect{
                logUtil.log(LoginViewModel.TAG, "onCollect${it}")
                _forgotPasswordSendEmail.value = ForgotPassSendEmailUIState.OnSuccess(it)
            }
        }
    }

    fun verifyEmailForgotPass(email: String,otpNumber: String){
        _forgotPasswordVerifyEmail.value = ForgotPassVerifyEmailUIState.IsLoading(true)
        viewModelScope.launch {
            getVerifyEmailForgotPassUseCase.invoke(email,otpNumber).catch {
                logUtil.log(LoginViewModel.TAG, "onError${it.message.toString()}")
                val error = ErrorResponseHandler(it)
                _forgotPasswordVerifyEmail.value =ForgotPassVerifyEmailUIState.OnFailure(error.getErrors().message.toString())
            }.collect{
                logUtil.log(LoginViewModel.TAG, "onCollect${it}")
                _forgotPasswordVerifyEmail.value = ForgotPassVerifyEmailUIState.OnSuccess(it)
            }
        }
    }
}