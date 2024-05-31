package com.example.ticketpartner.feature_scan_module.feature_login_scan.domain.model

sealed class GetCheckInListOfflineUIState {
    data class IsLoading(val isLoading: Boolean) : GetCheckInListOfflineUIState()
    data class OnSuccess(val onSuccess: GetCheckInDataOfflineScanResponse) : GetCheckInListOfflineUIState()
    data class OnFailure(val onFailure: String) : GetCheckInListOfflineUIState()
}