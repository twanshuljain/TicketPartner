package com.mtp.scanner.feature_scan_module.feature_login_scan.domain.model

sealed class InsertTicketTypesOfflineScanUIState {
    data class IsLoading(val isLoading: Boolean) : InsertTicketTypesOfflineScanUIState()
    data class OnSuccess(val onSuccess: String) : InsertTicketTypesOfflineScanUIState()
    data class OnFailure(val onFailure: String) : InsertTicketTypesOfflineScanUIState()
}