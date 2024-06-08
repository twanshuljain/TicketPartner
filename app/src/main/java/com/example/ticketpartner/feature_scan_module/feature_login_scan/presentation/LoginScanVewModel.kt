package com.example.ticketpartner.feature_scan_module.feature_login_scan.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ticketpartner.common.LogUtil
import com.example.ticketpartner.common.storage.MyPreferences
import com.example.ticketpartner.common.storage.PrefConstants
import com.example.ticketpartner.feature_local_storage.domain.usecase.GetEventDetailsLocalDBUseCase
import com.example.ticketpartner.feature_local_storage.domain.usecase.InsertEventDetailsUseCase
import com.example.ticketpartner.feature_local_storage.domain.usecase.InsertQrCodeListUseCase
import com.example.ticketpartner.feature_local_storage.domain.usecase.InsertTicketTypesOfflineUseCase
import com.example.ticketpartner.feature_scan_module.feature_login_scan.domain.model.CheckInData
import com.example.ticketpartner.feature_scan_module.feature_login_scan.domain.model.DataItems
import com.example.ticketpartner.feature_scan_module.feature_login_scan.domain.model.EventDetailsScanUIState
import com.example.ticketpartner.feature_scan_module.feature_login_scan.domain.model.GetCheckInDataOfflineScanUIState
import com.example.ticketpartner.feature_scan_module.feature_login_scan.domain.model.GetEventDetailsOfflineScanUIState
import com.example.ticketpartner.feature_scan_module.feature_login_scan.domain.model.GetQrCodeForOffLineScanUIState
import com.example.ticketpartner.feature_scan_module.feature_login_scan.domain.model.GetTicketTypesOfflineScanUIState
import com.example.ticketpartner.feature_scan_module.feature_login_scan.domain.model.InsertCheckInDataOfflineUIState
import com.example.ticketpartner.feature_scan_module.feature_login_scan.domain.model.InsertEventDetailsResponse
import com.example.ticketpartner.feature_scan_module.feature_login_scan.domain.model.InsertEventDetailsUIState
import com.example.ticketpartner.feature_scan_module.feature_login_scan.domain.model.InsertQrCodeForOffLineScanUIState
import com.example.ticketpartner.feature_scan_module.feature_login_scan.domain.model.InsertTicketTypeListResponse
import com.example.ticketpartner.feature_scan_module.feature_login_scan.domain.model.LoginWithPinUIState
import com.example.ticketpartner.feature_scan_module.feature_login_scan.domain.usecase.GetCheckInListOfflineUseCase
import com.example.ticketpartner.feature_scan_module.feature_login_scan.domain.usecase.GetLoginWithPinUseCase
import com.example.ticketpartner.feature_scan_module.feature_login_scan.domain.usecase.GetQrCodeListUseCase
import com.example.ticketpartner.feature_scan_module.feature_login_scan.domain.usecase.GetScanEventDetailsUseCase
import com.example.ticketpartner.feature_scan_module.feature_login_scan.domain.usecase.InsertCheckInDataOfflineUseCase
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
    private val insertEventDetailsUseCase: InsertEventDetailsUseCase,
    private val getEventDetailsLocalDBUseCase: GetEventDetailsLocalDBUseCase,
    private val insertTicketTypesOfflineUseCase: InsertTicketTypesOfflineUseCase,
    private val getCheckInListOfflineUseCase: GetCheckInListOfflineUseCase,
    private val insertCheckInDataOfflineUseCase: InsertCheckInDataOfflineUseCase,
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

    private val _getCheckInDataForOfflineScan: MutableLiveData<GetCheckInDataOfflineScanUIState> =
        MutableLiveData()
    val getCheckInDataForOfflineScan: LiveData<GetCheckInDataOfflineScanUIState> =
        _getCheckInDataForOfflineScan

    private val _insertQrCodeListForOfflineScan: MutableLiveData<InsertQrCodeForOffLineScanUIState> =
        MutableLiveData()
    val insertQrCodeListForOfflineScan: LiveData<InsertQrCodeForOffLineScanUIState> =
        _insertQrCodeListForOfflineScan

    private val _insertEventDetailsOfflineScan: MutableLiveData<InsertEventDetailsUIState> =
        MutableLiveData()
    val insertEventDetailsOfflineScan: LiveData<InsertEventDetailsUIState> =
        _insertEventDetailsOfflineScan

    private val _getEventDetailsOfflineScan: MutableLiveData<GetEventDetailsOfflineScanUIState> =
        MutableLiveData()
    val getEventDetailsOfflineScan: LiveData<GetEventDetailsOfflineScanUIState> =
        _getEventDetailsOfflineScan

    private val _insertTicketTypesOfflineScan: MutableLiveData<GetTicketTypesOfflineScanUIState> =
        MutableLiveData()
    val insertTicketTypesOfflineScan: LiveData<GetTicketTypesOfflineScanUIState> =
        _insertTicketTypesOfflineScan

    private val _insertCheckInDataOfflineScan: MutableLiveData<InsertCheckInDataOfflineUIState> =
        MutableLiveData()
    val insertCheckInDataOfflineScan: LiveData<InsertCheckInDataOfflineUIState> =
        _insertCheckInDataOfflineScan

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

    fun getCheckInDataForOffline(){
        _getCheckInDataForOfflineScan.value = GetCheckInDataOfflineScanUIState.IsLoading(true)
        viewModelScope.launch {
            getCheckInListOfflineUseCase.invoke().catch {
                logUtil.log(TAG, "onError${it.message.toString()}")
                val error = ErrorResponseHandler(it)
                _getCheckInDataForOfflineScan.value =
                    GetCheckInDataOfflineScanUIState.OnFailure(error.getErrors().message.toString())
            }.collect{
                logUtil.log(TAG, "onResponse: $it")
                _getCheckInDataForOfflineScan.value = GetCheckInDataOfflineScanUIState.OnSuccess(it)
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


    fun insertEventDetailsForOfflineScan(insertEventDetailsResponse: InsertEventDetailsResponse) {
        _insertEventDetailsOfflineScan.value = InsertEventDetailsUIState.IsLoading(true)
        viewModelScope.launch {
            insertEventDetailsUseCase.invoke(insertEventDetailsResponse).catch {
                logUtil.log(TAG, "onError${it.message.toString()}")
                _insertEventDetailsOfflineScan.value =
                    InsertEventDetailsUIState.OnFailure(it.message.toString())
            }.collect {
                _insertEventDetailsOfflineScan.value =
                    InsertEventDetailsUIState.OnSuccess("Event Detail's data inserted successfully!")
            }
        }
    }

    fun insetQrCodeListForOfflineScan(getQrCodeListResponse: DataItems) {
        _insertQrCodeListForOfflineScan.value = InsertQrCodeForOffLineScanUIState.IsLoading(true)
        viewModelScope.launch {
            insertQrCodeListUseCase.invoke(getQrCodeListResponse).catch {
                logUtil.log(TAG, "onError${it.message.toString()}")
                //val error = ErrorResponseHandler(it)
                _insertQrCodeListForOfflineScan.value =
                    InsertQrCodeForOffLineScanUIState.OnFailure(it.message.toString())
            }.collect {
                logUtil.log(TAG, "onResponse: $it")
                _insertQrCodeListForOfflineScan.value =
                    InsertQrCodeForOffLineScanUIState.OnSuccess("Data inserted successfully!")
            }
        }
    }



    fun getEventDetailsOfflineScan() {
        _getEventDetailsOfflineScan.value = GetEventDetailsOfflineScanUIState.IsLoading(true)
        viewModelScope.launch {
            getEventDetailsLocalDBUseCase.invoke().catch {
                logUtil.log(TAG, "onError${it.message.toString()}")
                _getEventDetailsOfflineScan.value =
                    GetEventDetailsOfflineScanUIState.OnFailure(it.message.toString())
            }.collect {
                logUtil.log(TAG, "onResponse: $it")
                _getEventDetailsOfflineScan.value = GetEventDetailsOfflineScanUIState.OnSuccess(it)
            }
        }
    }

    fun insertTicketTypesOfflineScan(ticketTypeList: InsertTicketTypeListResponse) {
        _insertTicketTypesOfflineScan.value = GetTicketTypesOfflineScanUIState.IsLoading(true)
        viewModelScope.launch {
            insertTicketTypesOfflineUseCase.invoke(ticketTypeList).catch {
                logUtil.log(TAG, "onError${it.message.toString()}")
                _insertTicketTypesOfflineScan.value =
                    GetTicketTypesOfflineScanUIState.OnFailure(it.message.toString())
            }.collect{
                logUtil.log(TAG, "onResponse: $it")
                _insertTicketTypesOfflineScan.value =
                    GetTicketTypesOfflineScanUIState.OnSuccess("Data inserted successfully!")
            }
        }
    }

    fun insertCheckInDataOfflineScan(checkInData: CheckInData){
        _insertCheckInDataOfflineScan.value = InsertCheckInDataOfflineUIState.IsLoading(true)
        viewModelScope.launch {
            insertCheckInDataOfflineUseCase.invoke(checkInData).catch {
                logUtil.log(TAG, "onError${it.message.toString()}")
                _insertCheckInDataOfflineScan.value =
                    InsertCheckInDataOfflineUIState.OnFailure(it.message.toString())
            }.collect{
                logUtil.log(TAG, "onResponse: $it")
                _insertCheckInDataOfflineScan.value =
                    InsertCheckInDataOfflineUIState.OnSuccess("Data inserted successfully!")
            }
        }
    }



    companion object {
        val TAG = LoginScanVewModel::class.java.simpleName
    }
}