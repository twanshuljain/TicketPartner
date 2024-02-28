package com.example.ticketpartner.feature_add_organization.domain.model

sealed class SearchCountryUIState {
    data class IsLoading(val isLoading: Boolean = false) : SearchCountryUIState()
    data class OnSuccess(val result: SearchCountryResponse) : SearchCountryUIState()
    data class OnFailure(val onFailure: String) : SearchCountryUIState()
}