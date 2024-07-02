package com.example.ticketpartner.feature_scan_module.feature_login_scan.domain.model

sealed class InsertScanReportTicketListUIState {
    data class IsLoading(val isLoading: Boolean) : InsertScanReportTicketListUIState()
    data class OnSuccess(val onSuccess: String) : InsertScanReportTicketListUIState()
    data class OnFailure(val onFailure: String) : InsertScanReportTicketListUIState()
}