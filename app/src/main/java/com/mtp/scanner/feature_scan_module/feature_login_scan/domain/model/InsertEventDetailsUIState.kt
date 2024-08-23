package com.mtp.scanner.feature_scan_module.feature_login_scan.domain.model

sealed class InsertEventDetailsUIState {
    data class IsLoading(val isLoading: Boolean) : InsertEventDetailsUIState()
    data class OnSuccess(val onSuccess: String) : InsertEventDetailsUIState()
    data class OnFailure(val onFailure: String) : InsertEventDetailsUIState()
}