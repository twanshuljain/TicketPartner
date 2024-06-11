package com.example.ticketpartner.feature_scan_module.feature_qr_scan.domain.model

import com.example.ticketpartner.feature_scan_module.feature_login_scan.domain.model.SearchData

sealed class GetScanSearchDataOfflineUIState {
    data class IsLoading(val isLoading: Boolean) : GetScanSearchDataOfflineUIState()
    data class OnSuccess(val onSuccess: List<SearchData?>?) : GetScanSearchDataOfflineUIState()
    data class OnFailure(val onFailure: String) : GetScanSearchDataOfflineUIState()
}