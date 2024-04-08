package com.example.ticketpartner.scan_module.domain.model

sealed class EventDetailsScanUIState {
    data class IsLoading(val isLoading: Boolean) : EventDetailsScanUIState()
    data class OnSuccess(val onSuccess: EventDetailsScanResponse) : EventDetailsScanUIState()
    data class OnFailure(val onFailure: String) : EventDetailsScanUIState()
}