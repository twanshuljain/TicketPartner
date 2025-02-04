package com.mtp.scanner.feature_scan_module.feature_qr_scan.domain.model

import com.mtp.scanner.feature_scan_module.feature_login_scan.domain.model.InsertScanReportDataResponse

sealed class GetScanReportDataOfflineUIState {
    data class IsLoading(val isLoading: Boolean) : GetScanReportDataOfflineUIState()
    data class OnSuccess(val onSuccess: List<InsertScanReportDataResponse>) : GetScanReportDataOfflineUIState()
    data class OnFailure(val onFailure: String) : GetScanReportDataOfflineUIState()
}