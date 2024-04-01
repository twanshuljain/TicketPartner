package com.example.ticketpartner.feature_create_event.domain.model

sealed class CreateEventTicketListUIState {
    data class IsLoading(val isLoading: Boolean = false) : CreateEventTicketListUIState()
    data class OnSuccess(val result: CreateEventTicketListResponse) :
        CreateEventTicketListUIState()

    data class OnFailure(val onFailure: String) : CreateEventTicketListUIState()
}