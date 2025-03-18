package com.mtp.ticketpartner.scanner.feature_scan_module.feature_login_scan.domain.model

sealed class EventDetailsScanUIState {
    data class IsLoading(val isLoading: Boolean) : EventDetailsScanUIState()
    data class OnSuccess(val onSuccess: EventDetailsScanResponse) : EventDetailsScanUIState()
    data class OnFailure(val onFailure: String) : EventDetailsScanUIState()
}