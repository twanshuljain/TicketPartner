package com.example.ticketpartner.feature_scan_module.feature_login_scan.domain.model

sealed class GetTicketTypesOfflineScanUIState {
    data class IsLoading(val isLoading: Boolean) : GetTicketTypesOfflineScanUIState()
    data class OnSuccess(val onSuccess: String) : GetTicketTypesOfflineScanUIState()
    data class OnFailure(val onFailure: String) : GetTicketTypesOfflineScanUIState()
}