package com.example.ticketpartner.scan_module.feature_qr_scan.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ticketpartner.common.LogUtil
import com.example.ticketpartner.scan_module.feature_qr_scan.domain.model.QrScanUIState
import com.example.ticketpartner.scan_module.feature_qr_scan.domain.model.QrScannedTicketUIState
import com.example.ticketpartner.scan_module.feature_qr_scan.domain.usecase.GetQrScanUseCase
import com.example.ticketpartner.scan_module.feature_qr_scan.domain.usecase.GetQrScannedTicketDataUseCase
import com.technotoil.tglivescan.common.retrofit.apis.ErrorResponseHandler
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class QrScanViewModel @Inject constructor(
    private val getQrScanUseCase: GetQrScanUseCase,
    private val getQrScannedTicketDataUseCase: GetQrScannedTicketDataUseCase,
    private val logUtil: LogUtil
) : ViewModel() {

    private val _qrScanState: MutableLiveData<QrScanUIState> = MutableLiveData()
    val observeQrScanResponse: LiveData<QrScanUIState> = _qrScanState

    private val _getScannedTicketState: MutableLiveData<QrScannedTicketUIState> = MutableLiveData()
    val observeTicketScannedResponse: LiveData<QrScannedTicketUIState> = _getScannedTicketState

    fun qrScanCode(qrId: String, ticketType: ArrayList<String>){
        _qrScanState.value = QrScanUIState.IsLoading(true)
        viewModelScope.launch {
            getQrScanUseCase.invoke(qrId, ticketType).catch {
                logUtil.log(TAG, "onError${it.message.toString()}")
                val error = ErrorResponseHandler(it)
                _qrScanState.value =
                    QrScanUIState.OnFailure(error.getErrors().message.toString())
            }.collect{
                logUtil.log(TAG, "onResponse: $it")
                _qrScanState.value = QrScanUIState.OnSuccess(it)
            }
        }
    }

    fun getScannedTicketData(){
        _getScannedTicketState.value = QrScannedTicketUIState.IsLoading(true)
        viewModelScope.launch {
            getQrScannedTicketDataUseCase.invoke().catch {
                logUtil.log(TAG, "onError${it.message.toString()}")
                val error = ErrorResponseHandler(it)
                _getScannedTicketState.value =
                    QrScannedTicketUIState.OnFailure(error.getErrors().message.toString())
            }.collect{
                logUtil.log(TAG, "onResponse: $it")
                _getScannedTicketState.value = QrScannedTicketUIState.OnSuccess(it)
            }
        }
    }

    companion object {
        val TAG = QrScanViewModel::class.java.simpleName
    }
}