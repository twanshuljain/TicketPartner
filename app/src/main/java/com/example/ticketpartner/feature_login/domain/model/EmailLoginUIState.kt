package com.example.ticketpartner.feature_login.domain.model

sealed class EmailLoginUIState {

    data class IsLoading(val isLoading: Boolean = false) : EmailLoginUIState()
    data class OnSuccess(val result: UserLoginResponse) : EmailLoginUIState()
    data class OnFailure(val onFailure: String) : EmailLoginUIState()

}