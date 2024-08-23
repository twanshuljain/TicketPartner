package com.mtp.scanner.feature_scan_module.feature_login_scan.domain.model

sealed class InsertCheckInDataOfflineUIState {
    data class IsLoading(val isLoading: Boolean) : InsertCheckInDataOfflineUIState()
    data class OnSuccess(val onSuccess: String) : InsertCheckInDataOfflineUIState()
    data class OnFailure(val onFailure: String) : InsertCheckInDataOfflineUIState()
}