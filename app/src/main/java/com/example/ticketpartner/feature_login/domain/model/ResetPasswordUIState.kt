package com.example.ticketpartner.feature_login.domain.model

sealed class ResetPasswordUIState {
    data class IsLoading(val isLoading: Boolean = false) : ResetPasswordUIState()
    data class OnSuccess(val result: ResetPasswordResponse) : ResetPasswordUIState()
    data class OnFailure(val onFailure: String) : ResetPasswordUIState()
}