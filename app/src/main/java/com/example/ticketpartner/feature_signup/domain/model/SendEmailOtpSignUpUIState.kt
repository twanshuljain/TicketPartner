package com.example.ticketpartner.feature_signup.domain.model

sealed class SendEmailOtpSignUpUIState {
    data class IsLoading(val isLoading: Boolean = false) : SendEmailOtpSignUpUIState()
    data class OnSuccess(val result: SendEmailOtpSignUpResponse) : SendEmailOtpSignUpUIState()
    data class OnFailure(val onFailure: String) : SendEmailOtpSignUpUIState()
}