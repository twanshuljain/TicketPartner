package com.mtp.ticketpartner.scanner.feature_scan_module.feature_login_scan.domain.model

import com.mtp.ticketpartner.scanner.feature_scan_module.feature_qr_scan.domain.model.SearchApiScanResponse

sealed class QrScanSearchItemUIState {
    data class IsLoading(val isLoading: Boolean) : QrScanSearchItemUIState()
    data class OnSuccess(val onSuccess: SearchApiScanResponse) : QrScanSearchItemUIState()
    data class OnFailure(val onFailure: String) : QrScanSearchItemUIState()
}