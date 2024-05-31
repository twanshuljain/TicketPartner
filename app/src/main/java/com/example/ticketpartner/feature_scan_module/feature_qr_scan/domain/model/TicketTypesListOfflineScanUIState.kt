package com.example.ticketpartner.feature_scan_module.feature_qr_scan.domain.model

import com.example.ticketpartner.feature_scan_module.feature_login_scan.domain.model.InsertTicketTypeListResponse

sealed class TicketTypesListOfflineScanUIState {
    data class IsLoading(val isLoading: Boolean) : TicketTypesListOfflineScanUIState()
    data class OnSuccess(val onSuccess: List<InsertTicketTypeListResponse>) : TicketTypesListOfflineScanUIState()
    data class OnFailure(val onFailure: String) : TicketTypesListOfflineScanUIState()
}