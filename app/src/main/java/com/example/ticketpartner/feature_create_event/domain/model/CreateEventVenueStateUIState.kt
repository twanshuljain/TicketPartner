package com.example.ticketpartner.feature_create_event.domain.model

sealed class CreateEventVenueStateUIState {
    data class IsLoading(val isLoading: Boolean = false) : CreateEventVenueStateUIState()
    data class OnSuccess(val result: CreateEventVenueStateResponse) : CreateEventVenueStateUIState()
    data class OnFailure(val onFailure: String) : CreateEventVenueStateUIState()
}