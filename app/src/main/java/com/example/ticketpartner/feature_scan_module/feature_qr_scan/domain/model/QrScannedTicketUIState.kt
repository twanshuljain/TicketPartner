package com.example.ticketpartner.feature_scan_module.feature_qr_scan.domain.model

sealed class QrScannedTicketUIState {
    data class IsLoading(val isLoading: Boolean) : QrScannedTicketUIState()
    data class OnSuccess(val onSuccess: QrScannedTicketResponse) : QrScannedTicketUIState()
    data class OnFailure(val onFailure: String) : QrScannedTicketUIState()
}