package com.example.ticketpartner.scan_module.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ticketpartner.common.LogUtil
import com.example.ticketpartner.common.storage.MyPreferences
import com.example.ticketpartner.common.storage.PrefConstants
import com.example.ticketpartner.feature_login.presentation.LoginViewModel
import com.example.ticketpartner.scan_module.domain.model.EventDetailsScanUIState
import com.example.ticketpartner.scan_module.domain.model.LoginWithPinUIState
import com.example.ticketpartner.scan_module.domain.usecase.GetLoginWithPinUseCase
import com.example.ticketpartner.scan_module.domain.usecase.GetScanEventDetailsUseCase
import com.technotoil.tglivescan.common.retrofit.apis.ErrorResponseHandler
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginScanVewModel @Inject constructor(
    private val getPinLoginUseCase: GetLoginWithPinUseCase,
    private val getScanEventDetailsUseCase: GetScanEventDetailsUseCase,
    private val logUtil: LogUtil
) :
    ViewModel() {
    private val _pinLoginState: MutableLiveData<LoginWithPinUIState> = MutableLiveData()
    val observePinLoginResponse: LiveData<LoginWithPinUIState> = _pinLoginState

    private val _getScanEventDetails: MutableLiveData<EventDetailsScanUIState> = MutableLiveData()
    val observeScanEventDetailsResponse: LiveData<EventDetailsScanUIState> = _getScanEventDetails


    fun loginWithPin(name: String, scanPin: String) {
        _pinLoginState.value = LoginWithPinUIState.IsLoading(true)
        viewModelScope.launch {
            getPinLoginUseCase.invoke(name, scanPin).catch {
                logUtil.log(LoginViewModel.TAG, "onError${it.message.toString()}")
                val error = ErrorResponseHandler(it)
                _pinLoginState.value =
                    LoginWithPinUIState.OnFailure(error.getErrors().message.toString())
            }.collect {
                it.data?.access_token?.let { accessToken ->
                    MyPreferences.putString(
                        PrefConstants.ACCESS_TOKEN, accessToken.toString()
                    )
                    _pinLoginState.value = LoginWithPinUIState.OnSuccess(it)
                }
            }
        }
    }

    fun getEventDetailsData() {
        _getScanEventDetails.value = EventDetailsScanUIState.IsLoading(true)
        viewModelScope.launch {
            getScanEventDetailsUseCase.invoke().catch {
                logUtil.log(LoginViewModel.TAG, "onError${it.message.toString()}")
                val error = ErrorResponseHandler(it)
                _getScanEventDetails.value =
                    EventDetailsScanUIState.OnFailure(error.getErrors().message.toString())
            }.collect {
                _getScanEventDetails.value = EventDetailsScanUIState.OnSuccess(it)
            }
        }
    }
}