package com.example.ticketpartner.feature_login.domain.model

sealed class ForgotPassSendEmailUIState {
    data class IsLoading(val isLoading: Boolean = false) : ForgotPassSendEmailUIState()
    data class OnSuccess(val result: ForgotPassSendEmailResponse) : ForgotPassSendEmailUIState()
    data class OnFailure(val onFailure: String) : ForgotPassSendEmailUIState()
}