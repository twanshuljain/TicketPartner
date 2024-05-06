package com.example.ticketpartner.feature_scan_module.feature_qr_scan.domain.model

sealed class QrScanOrderDetailsUIState {
    data class IsLoading(val isLoading: Boolean) : QrScanOrderDetailsUIState()
    data class OnSuccess(val onSuccess: ScanSearchOrderDetailsResponse) : QrScanOrderDetailsUIState()
    data class OnFailure(val onFailure: String) : QrScanOrderDetailsUIState()
}