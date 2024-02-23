package com.example.ticketpartner.feature_login.domain.model

sealed class ForgotPassVerifyEmailUIState {
    data class IsLoading(val isLoading: Boolean = false) : ForgotPassVerifyEmailUIState()
    data class OnSuccess(val result: VerifyEmailForgotPassResponse) : ForgotPassVerifyEmailUIState()
    data class OnFailure(val onFailure: String) : ForgotPassVerifyEmailUIState()
}