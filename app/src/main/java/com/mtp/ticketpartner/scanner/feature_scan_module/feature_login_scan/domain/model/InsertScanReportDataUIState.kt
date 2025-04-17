package com.mtp.ticketpartner.scanner.feature_scan_module.feature_login_scan.domain.model

sealed class InsertScanReportDataUIState {
    data class IsLoading(val isLoading: Boolean) : InsertScanReportDataUIState()
    data class OnSuccess(val onSuccess: String) : InsertScanReportDataUIState()
    data class OnFailure(val onFailure: String) : InsertScanReportDataUIState()
}