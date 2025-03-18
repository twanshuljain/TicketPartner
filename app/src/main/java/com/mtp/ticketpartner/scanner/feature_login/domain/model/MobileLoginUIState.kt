package com.mtp.ticketpartner.scanner.feature_login.domain.model

sealed class MobileLoginUIState {

    data class IsLoading(val isLoading: Boolean) : MobileLoginUIState()
    data class OnSuccess(val onSuccess: UserLoginPhoneResponse) : MobileLoginUIState()
    data class OnFailure(val onFailure: String) : MobileLoginUIState()
}