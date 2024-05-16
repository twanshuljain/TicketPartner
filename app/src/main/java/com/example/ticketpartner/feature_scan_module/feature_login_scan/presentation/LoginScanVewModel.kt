package com.example.ticketpartner.feature_scan_module.feature_login_scan.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ticketpartner.common.LogUtil
import com.example.ticketpartner.common.storage.MyPreferences
import com.example.ticketpartner.common.storage.PrefConstants
import com.example.ticketpartner.feature_local_storage.domain.usecase.InsertQrCodeListUseCase
import com.example.ticketpartner.feature_scan_module.feature_login_scan.domain.model.DataItems
import com.example.ticketpartner.feature_scan_module.feature_login_scan.domain.model.EventDetailsScanUIState
import com.example.ticketpartner.feature_scan_module.feature_login_scan.domain.model.GetQrCodeForOffLineScanUIState
import com.example.ticketpartner.feature_scan_module.feature_login_scan.domain.model.InsertQrCodeForOffLineScanUIState
import com.example.ticketpartner.feature_scan_module.feature_login_scan.domain.model.LoginWithPinUIState
import com.example.ticketpartner.feature_scan_module.feature_login_scan.domain.usecase.GetLoginWithPinUseCase
import com.example.ticketpartner.feature_scan_module.feature_login_scan.domain.usecase.GetQrCodeListUseCase
import com.example.ticketpartner.feature_scan_module.feature_login_scan.domain.usecase.GetScanEventDetailsUseCase
import com.technotoil.tglivescan.common.retrofit.apis.ErrorResponseHandler
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginScanVewModel @Inject constructor(
    private val getPinLoginUseCase: GetLoginWithPinUseCase,
    private val getScanEventDetailsUseCase: GetScanEventDetailsUseCase,
    private val getQrCodeListUseCase: GetQrCodeListUseCase,
    private val insertQrCodeListUseCase: InsertQrCodeListUseCase,
    private val logUtil: LogUtil
) :
    ViewModel() {
    private val _pinLoginState: MutableLiveData<LoginWithPinUIState> = MutableLiveData()
    val observePinLoginResponse: LiveData<LoginWithPinUIState> = _pinLoginState

    private val _getScanEventDetails: MutableLiveData<EventDetailsScanUIState> = MutableLiveData()
    val observeScanEventDetailsResponse: LiveData<EventDetailsScanUIState> = _getScanEventDetails

    private val _getQrCodeListForOfflineScan: MutableLiveData<GetQrCodeForOffLineScanUIState> =
        MutableLiveData()
    val getQrCodeListForOfflineScan: LiveData<GetQrCodeForOffLineScanUIState> =
        _getQrCodeListForOfflineScan

    private val _getCheckInDataForOfflineScan: MutableLiveData<GetQrCodeForOffLineScanUIState> =
        MutableLiveData()
    val getCheckInDataForOfflineScan: LiveData<GetQrCodeForOffLineScanUIState> =
        _getCheckInDataForOfflineScan

    private val _insertQrCodeListForOfflineScan: MutableLiveData<InsertQrCodeForOffLineScanUIState> =
        MutableLiveData()
    val insertQrCodeListForOfflineScan: LiveData<InsertQrCodeForOffLineScanUIState> =
        _insertQrCodeListForOfflineScan


    fun loginWithPin(name: String, scanPin: String) {
        _pinLoginState.value = LoginWithPinUIState.IsLoading(true)
        viewModelScope.launch {
            getPinLoginUseCase.invoke(name, scanPin).catch {
                logUtil.log(TAG, "onError${it.message.toString()}")
                val error = ErrorResponseHandler(it)
                _pinLoginState.value =
                    LoginWithPinUIState.OnFailure(error.getErrors().message.toString())
            }.collect {
                logUtil.log(TAG, "onResponse: $it")
                it.data?.access_token?.let { accessToken ->
                    MyPreferences.putString(
                        PrefConstants.ACCESS_TOKEN, accessToken
                    )
                    _pinLoginState.value = LoginWithPinUIState.OnSuccess(it)
                }
                logUtil.log(
                    TAG,
                    "accessToken: ${MyPreferences.getString(PrefConstants.ACCESS_TOKEN)}"
                )
            }
        }
    }

    fun getEventDetailsData() {
        _getScanEventDetails.value = EventDetailsScanUIState.IsLoading(true)
        viewModelScope.launch {
            getScanEventDetailsUseCase.invoke().catch {
                logUtil.log(TAG, "onError${it.message.toString()}")
                val error = ErrorResponseHandler(it)
                _getScanEventDetails.value =
                    EventDetailsScanUIState.OnFailure(error.getErrors().message.toString())
            }.collect {
                logUtil.log(TAG, "onResponse: $it")
                _getScanEventDetails.value = EventDetailsScanUIState.OnSuccess(it)
            }
        }
    }

    fun getQrCodeListForOfflineScan() {
        _getQrCodeListForOfflineScan.value = GetQrCodeForOffLineScanUIState.IsLoading(true)
        viewModelScope.launch {
            getQrCodeListUseCase.invoke().catch {
                logUtil.log(TAG, "onError${it.message.toString()}")
                val error = ErrorResponseHandler(it)
                _getQrCodeListForOfflineScan.value =
                    GetQrCodeForOffLineScanUIState.OnFailure(error.getErrors().message.toString())
            }.collect {
                logUtil.log(TAG, "onResponse: $it")
                _getQrCodeListForOfflineScan.value = GetQrCodeForOffLineScanUIState.OnSuccess(it)
            }
        }
    }

    fun getCheckInDataForOfflineScan(){

    }

    fun insetQrCodeListForOfflineScan(getQrCodeListResponse: DataItems) {
        _insertQrCodeListForOfflineScan.value = InsertQrCodeForOffLineScanUIState.IsLoading(true)
        viewModelScope.launch {
            insertQrCodeListUseCase.invoke(getQrCodeListResponse).catch {
                logUtil.log(TAG, "onError${it.message.toString()}")
            //    val error = ErrorResponseHandler(it)
                _insertQrCodeListForOfflineScan.value =
                    InsertQrCodeForOffLineScanUIState.OnFailure(it.message.toString())
            }.collect {
                logUtil.log(TAG, "onResponse: $it")
                _insertQrCodeListForOfflineScan.value = InsertQrCodeForOffLineScanUIState.OnSuccess("Data inserted successfully!")
            }
        }
    }

    companion object {
        val TAG = LoginScanVewModel::class.java.simpleName
    }
}