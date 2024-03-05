package com.example.ticketpartner.feature_create_event.domain.model

sealed class CreateEventGetTimeZoneUIState {
    data class IsLoading(val isLoading: Boolean = false) : CreateEventGetTimeZoneUIState()
    data class OnSuccess(val result: CreateEventGetTimeZoneResponse) :
        CreateEventGetTimeZoneUIState()

    data class OnFailure(val onFailure: String) : CreateEventGetTimeZoneUIState()
}