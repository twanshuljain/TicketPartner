package com.example.ticketpartner.feature_login.domain.model

sealed class SendEmailOtpVerifyUIState {
    data class IsLoading(val isLoading: Boolean = false) : SendEmailOtpVerifyUIState()
    data class OnSuccess(val result: SendEmailOtpVerifyResponse) : SendEmailOtpVerifyUIState()
    data class OnFailure(val onFailure: String) : SendEmailOtpVerifyUIState()
}