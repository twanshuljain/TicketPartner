package com.example.ticketpartner.feature_scan_module.feature_qr_scan.domain.model

sealed class QrScanSearchItemUIState {
    data class IsLoading(val isLoading: Boolean) : QrScanSearchItemUIState()
    data class OnSuccess(val onSuccess: SearchApiScanResponse) : QrScanSearchItemUIState()
    data class OnFailure(val onFailure: String) : QrScanSearchItemUIState()
}