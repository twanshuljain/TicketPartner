package com.example.ticketpartner.feature_scan_module.feature_qr_scan.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ticketpartner.common.LogUtil
import com.example.ticketpartner.feature_scan_module.feature_login_scan.domain.model.DataItem
import com.example.ticketpartner.feature_scan_module.feature_login_scan.domain.model.EventDetailsScanUIState
import com.example.ticketpartner.feature_scan_module.feature_qr_scan.domain.model.QrScanSearchItemUIState
import com.example.ticketpartner.feature_scan_module.feature_qr_scan.domain.model.QrScanUIState
import com.example.ticketpartner.feature_scan_module.feature_qr_scan.domain.model.QrScannedTicketUIState
import com.example.ticketpartner.feature_scan_module.feature_qr_scan.domain.usecase.GetQrScanSearchUseCase
import com.example.ticketpartner.feature_scan_module.feature_qr_scan.domain.usecase.GetQrScanUseCase
import com.example.ticketpartner.feature_scan_module.feature_qr_scan.domain.usecase.GetQrScannedTicketDataUseCase
import com.example.ticketpartner.feature_scan_module.feature_qr_scan.domain.usecase.GetScanEventDetailsDashboardUseCase
import com.technotoil.tglivescan.common.retrofit.apis.ErrorResponseHandler
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class QrScanViewModel @Inject constructor(
    private val getQrScanUseCase: GetQrScanUseCase,
    private val getQrScannedTicketDataUseCase: GetQrScannedTicketDataUseCase,
    private val getScanEventDetailsDashboardUseCase: GetScanEventDetailsDashboardUseCase,
    private val getQrScanSearchUseCase: GetQrScanSearchUseCase,
    private val logUtil: LogUtil
) : ViewModel() {

    private val _selectedTicketName: MutableLiveData<List<DataItem?>?> = MutableLiveData()
    val observerSelectedTicketName: LiveData<List<DataItem?>?> = _selectedTicketName

    private val _qrScanState: MutableLiveData<QrScanUIState> = MutableLiveData()
    val observeQrScanResponse: LiveData<QrScanUIState> = _qrScanState

    private val _getScannedTicketState: MutableLiveData<QrScannedTicketUIState> = MutableLiveData()
    val observeTicketScannedResponse: LiveData<QrScannedTicketUIState> = _getScannedTicketState

    private val _getScanEventDetails: MutableLiveData<EventDetailsScanUIState> = MutableLiveData()
    val observeScanEventDetailsResponse: LiveData<EventDetailsScanUIState> = _getScanEventDetails

    private val _getScanSearchData: MutableLiveData<QrScanSearchItemUIState> = MutableLiveData()
    val observeScanSearchData: LiveData<QrScanSearchItemUIState> = _getScanSearchData

    fun putSelectedTicketName(selectedTicketName: ArrayList<DataItem>) {
        _selectedTicketName.value = selectedTicketName
    }

    val onContinueClick = MutableLiveData<Boolean>()


    /*  private val _onContinueClick:MutableLiveData<Boolean> = MutableLiveData()
      val observeOnContinueClick:LiveData<Boolean> = _onContinueClick

      fun onContinueClick(valueM: Boolean){
          _onContinueClick.value = valueM
      }*/


    fun qrScanCode(qrId: String, ticketType: ArrayList<String>) {
        _qrScanState.value = QrScanUIState.IsLoading(true)
        viewModelScope.launch {
            getQrScanUseCase.invoke(qrId, ticketType).catch {
                logUtil.log(TAG, "onError${it.message.toString()}")
                val error = ErrorResponseHandler(it)
                _qrScanState.value =
                    QrScanUIState.OnFailure(error.getErrors().message.toString())
            }.collect {
                logUtil.log(TAG, "onResponse: $it")
                _qrScanState.value = QrScanUIState.OnSuccess(it)
            }
        }
    }

    fun getScannedTicketData() {
        _getScannedTicketState.value = QrScannedTicketUIState.IsLoading(true)
        viewModelScope.launch {
            getQrScannedTicketDataUseCase.invoke().catch {
                logUtil.log(TAG, "onError${it.message.toString()}")
                val error = ErrorResponseHandler(it)
                _getScannedTicketState.value =
                    QrScannedTicketUIState.OnFailure(error.getErrors().message.toString())
            }.collect {
                logUtil.log(TAG, "onResponse: $it")
                _getScannedTicketState.value = QrScannedTicketUIState.OnSuccess(it)
            }
        }
    }

    fun getEventDetailsData() {
        _getScanEventDetails.value = EventDetailsScanUIState.IsLoading(true)
        viewModelScope.launch {
            getScanEventDetailsDashboardUseCase.invoke().catch {
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

    fun getSearchData(orderId: String) {
        viewModelScope.launch {
            getQrScanSearchUseCase.invoke(orderId).catch {
                logUtil.log(TAG, "onError${it.message.toString()}")
                val error = ErrorResponseHandler(it)
                _getScanSearchData.value =
                    QrScanSearchItemUIState.OnFailure(error.getErrors().message.toString())
            }.collect {
                logUtil.log(TAG, "onResponse: $it")
                _getScanSearchData.value = QrScanSearchItemUIState.OnSuccess(it)
            }
        }
    }

    companion object {
        val TAG = QrScanViewModel::class.java.simpleName
    }
}