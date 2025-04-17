package com.mtp.ticketpartner.scanner.feature_scan_module.feature_login_scan.domain.model

sealed class GetCheckInDataOfflineScanUIState {
    data class IsLoading(val isLoading: Boolean) : GetCheckInDataOfflineScanUIState()
    data class OnSuccess(val onSuccess: GetCheckInDataOfflineResponse) : GetCheckInDataOfflineScanUIState()
    data class OnFailure(val onFailure: String) : GetCheckInDataOfflineScanUIState()
}