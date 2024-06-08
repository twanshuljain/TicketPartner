package com.example.ticketpartner.feature_scan_module.feature_qr_scan.domain.model

import com.example.ticketpartner.feature_scan_module.feature_login_scan.domain.model.InsertTicketTypeListResponse

sealed class GetTicketTypesListOfflineUIState {
    data class IsLoading(val isLoading: Boolean) : GetTicketTypesListOfflineUIState()
    data class OnSuccess(val onSuccess: List<InsertTicketTypeListResponse>) : GetTicketTypesListOfflineUIState()
    data class OnFailure(val onFailure: String) : GetTicketTypesListOfflineUIState()
}