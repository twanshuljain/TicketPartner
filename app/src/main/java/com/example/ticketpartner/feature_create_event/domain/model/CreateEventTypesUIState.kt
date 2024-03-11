package com.example.ticketpartner.feature_create_event.domain.model

sealed class CreateEventTypesUIState {
    data class IsLoading(val isLoading: Boolean = false) : CreateEventTypesUIState()
    data class OnSuccess(val result: CreateEventTypesResponse) : CreateEventTypesUIState()
    data class OnFailure(val onFailure: String) : CreateEventTypesUIState()
}