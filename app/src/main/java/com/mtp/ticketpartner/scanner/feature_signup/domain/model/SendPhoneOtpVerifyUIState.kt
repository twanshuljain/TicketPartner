package com.mtp.ticketpartner.scanner.feature_signup.domain.model

sealed class SendPhoneOtpVerifyUIState {
    data class IsLoading(val isLoading: Boolean = false) : SendPhoneOtpVerifyUIState()
    data class OnSuccess(val result: SendPhoneSignUpOtpVerifyResponse) : SendPhoneOtpVerifyUIState()
    data class OnFailure(val onFailure: String) : SendPhoneOtpVerifyUIState()
}