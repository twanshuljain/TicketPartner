package com.example.ticketpartner.feature_scan_module.feature_login_scan.domain.model

sealed class GetQrCodeForOffLineScanUIState {
    data class IsLoading(val isLoading: Boolean) : GetQrCodeForOffLineScanUIState()
    data class OnSuccess(val onSuccess: GetQrCodeListResponse) : GetQrCodeForOffLineScanUIState()
    data class OnFailure(val onFailure: String) : GetQrCodeForOffLineScanUIState()
}