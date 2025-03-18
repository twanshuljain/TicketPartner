package com.mtp.ticketpartner.scanner.feature_scan_module.feature_qr_scan.domain.model

sealed class DeleteScanLogDataUIState {
    data class IsLoading(val isLoading: Boolean) : DeleteScanLogDataUIState()
    data class OnSuccess(val onSuccess: String) : DeleteScanLogDataUIState()
    data class OnFailure(val onFailure: String) : DeleteScanLogDataUIState()
}