package com.example.ticketpartner.feature_login.domain.model

sealed class ForgotPasswordSendEmailLinkUIState {
    data class IsLoading(val isLoading: Boolean) : ForgotPasswordSendEmailLinkUIState()
    data class OnSuccess(val onSuccess: SendEmailLinkForgotPasswordResponse) : ForgotPasswordSendEmailLinkUIState()
    data class OnFailure(val onFailure: String) : ForgotPasswordSendEmailLinkUIState()
}