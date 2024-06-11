package com.example.ticketpartner.feature_scan_module.feature_qr_scan.domain.model

sealed class UploadScanDataServerUIState {
    data class IsLoading(val isLoading: Boolean) : UploadScanDataServerUIState()
    data class OnSuccess(val onSuccess: UploadScanLogDataServerResponse) : UploadScanDataServerUIState()
    data class OnFailure(val onFailure: String) : UploadScanDataServerUIState()
}