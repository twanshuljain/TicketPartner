package com.example.ticketpartner.feature_login.domain.model

sealed class SendPhoneOtpSignUpUIState {
    data class IsLoading(val isLoading: Boolean = false) : SendPhoneOtpSignUpUIState()
    data class OnSuccess(val result: SendPhoneSignUpOtpResponse) : SendPhoneOtpSignUpUIState()
    data class OnFailure(val onFailure: String) : SendPhoneOtpSignUpUIState()
}