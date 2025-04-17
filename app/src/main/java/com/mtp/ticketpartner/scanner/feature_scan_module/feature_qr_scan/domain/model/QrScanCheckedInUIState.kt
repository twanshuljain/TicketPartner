package com.mtp.ticketpartner.scanner.feature_scan_module.feature_qr_scan.domain.model

sealed class QrScanCheckedInUIState {
    data class IsLoading(val isLoading: Boolean) : QrScanCheckedInUIState()
    data class OnSuccess(val onSuccess: ScanCheckedInResponse) : QrScanCheckedInUIState()
    data class OnFailure(val onFailure: String) : QrScanCheckedInUIState()
}