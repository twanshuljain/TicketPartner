package com.mtp.ticketpartner.scanner.feature_scan_module.feature_qr_scan.domain.model

sealed class GetScanReportTicketListOfflineUIState {
    data class IsLoading(val isLoading: Boolean) : GetScanReportTicketListOfflineUIState()
    data class OnSuccess(val onSuccess: List<TicketDataList>) : GetScanReportTicketListOfflineUIState()
    data class OnFailure(val onFailure: String) : GetScanReportTicketListOfflineUIState()
}