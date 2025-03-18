package com.mtp.ticketpartner.scanner.feature_scan_module.feature_login_scan.domain.model

sealed class LoginWithPinUIState {
    data class IsLoading(val isLoading: Boolean) : LoginWithPinUIState()
    data class OnSuccess(val onSuccess: LoginWithPinResponse) : LoginWithPinUIState()
    data class OnFailure(val onFailure: String) : LoginWithPinUIState()
}