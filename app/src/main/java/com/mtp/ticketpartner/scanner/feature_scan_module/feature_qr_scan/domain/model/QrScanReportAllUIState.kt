package com.mtp.ticketpartner.scanner.feature_scan_module.feature_qr_scan.domain.model

sealed class QrScanReportAllUIState {
    data class IsLoading(val isLoading: Boolean) : QrScanReportAllUIState()
    data class OnSuccess(val onSuccess: QrScanReportAllResponse) : QrScanReportAllUIState()
    data class OnFailure(val onFailure: String) : QrScanReportAllUIState()
}