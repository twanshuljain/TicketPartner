package com.example.ticketpartner.feature_login.domain.model

sealed class SendEmailOtpSignUpUIState {
    data class IsLoading(val isLoading: Boolean = false) : SendEmailOtpSignUpUIState()
    data class OnSuccess(val result: SendEmailOtpResponseSignUp) : SendEmailOtpSignUpUIState()
    data class OnFailure(val onFailure: String) : SendEmailOtpSignUpUIState()
}