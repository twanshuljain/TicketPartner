package com.example.ticketpartner.feature_login.domain.model

sealed class MobileLoginUIState {

    data class OnLoading(val isLoading: Boolean) : MobileLoginUIState()
    data class OnSuccess(val onSuccess: UserLoginPhoneResponse) : MobileLoginUIState()
    data class OnFailure(val onFailure: String) : MobileLoginUIState()
}