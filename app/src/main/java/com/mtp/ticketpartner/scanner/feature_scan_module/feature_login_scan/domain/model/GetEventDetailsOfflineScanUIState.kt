package com.mtp.ticketpartner.scanner.feature_scan_module.feature_login_scan.domain.model

sealed class GetEventDetailsOfflineScanUIState {
    data class IsLoading(val isLoading: Boolean) : GetEventDetailsOfflineScanUIState()
    data class OnSuccess(val onSuccess: InsertEventDetailsResponse) : GetEventDetailsOfflineScanUIState()
    data class OnFailure(val onFailure: String) : GetEventDetailsOfflineScanUIState()
}