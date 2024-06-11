package com.example.ticketpartner.feature_scan_module.feature_login_scan.domain.model

sealed class InsertSearchDataOfflineUIState {
    data class IsLoading(val isLoading: Boolean) : InsertSearchDataOfflineUIState()
    data class OnSuccess(val onSuccess: String) : InsertSearchDataOfflineUIState()
    data class OnFailure(val onFailure: String) : InsertSearchDataOfflineUIState()
}