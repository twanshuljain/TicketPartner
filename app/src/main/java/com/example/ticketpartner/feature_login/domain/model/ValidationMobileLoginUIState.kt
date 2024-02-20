package com.example.ticketpartner.feature_login.domain.model

sealed class ValidationMobileLoginUIState {
    data class IsLoading(val isLoading: Boolean ) : ValidationMobileLoginUIState()
    data class IsMobileEmpty(val isMobileEmpty: Boolean ) : ValidationMobileLoginUIState()
    data class IsCountryCodeEmpty(val isCountryCodeEmpty: Boolean ) : ValidationMobileLoginUIState()
    data class OnAllDataValid(val allDataValid: Boolean ) : ValidationMobileLoginUIState()
}