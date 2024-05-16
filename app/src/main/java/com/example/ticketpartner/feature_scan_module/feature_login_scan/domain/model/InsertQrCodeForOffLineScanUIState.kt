package com.example.ticketpartner.feature_scan_module.feature_login_scan.domain.model

sealed class InsertQrCodeForOffLineScanUIState {
    data class IsLoading(val isLoading: Boolean) : InsertQrCodeForOffLineScanUIState()
    data class OnSuccess(val onSuccess: String) : InsertQrCodeForOffLineScanUIState()
    data class OnFailure(val onFailure: String) : InsertQrCodeForOffLineScanUIState()
}