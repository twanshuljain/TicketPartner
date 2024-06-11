package com.example.ticketpartner.feature_scan_module.feature_qr_scan.domain.model

sealed class InsertScanLogOfflineUIState {
    data class IsLoading(val isLoading: Boolean) : InsertScanLogOfflineUIState()
    data class OnSuccess(val onSuccess: String) : InsertScanLogOfflineUIState()
    data class OnFailure(val onFailure: String) : InsertScanLogOfflineUIState()
}