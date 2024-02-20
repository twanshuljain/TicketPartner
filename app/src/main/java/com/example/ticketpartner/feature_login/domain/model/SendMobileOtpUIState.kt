package com.example.ticketpartner.feature_login.domain.model

sealed class SendMobileOtpUIState {
    data class IsLoading(val isLoading: Boolean) : SendMobileOtpUIState()
    data class OnSuccess(val onSuccess: VerifyMobileOtpResponse) : SendMobileOtpUIState()
    data class OnFailure(val onFailure: String) : SendMobileOtpUIState()

}
