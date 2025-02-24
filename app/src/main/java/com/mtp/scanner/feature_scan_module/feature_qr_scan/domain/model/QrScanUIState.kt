package com.mtp.scanner.feature_scan_module.feature_qr_scan.domain.model

sealed class QrScanUIState {
    data class IsLoading(val isLoading: Boolean) : QrScanUIState()
    data class OnSuccess(val onSuccess: QrScanResponse) : QrScanUIState()
    data class OnFailure(val onFailure: String,
                         val customerName: String, val isTransfer: Boolean,
                         val isRefunded: Boolean) : QrScanUIState()
}