package com.mtp.ticketpartner.scanner.feature_scan_module.feature_qr_scan.domain.model

sealed class GetScanLogOfflineUIState {
    data class IsLoading(val isLoading: Boolean) : GetScanLogOfflineUIState()
    data class OnSuccess(val onSuccess: List<ScanLog>) : GetScanLogOfflineUIState()
    data class OnFailure(val onFailure: String) : GetScanLogOfflineUIState()
}