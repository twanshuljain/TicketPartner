package com.example.ticketpartner.feature_login.domain.model

sealed class EmailLoginUIState {

    data class OnSuccess(val result: UserLoginResponse) : EmailLoginUIState()

    data class Loading(val isLoading: Boolean) : EmailLoginUIState()

    data class OnEmailEnter(val isValid: Boolean) : EmailLoginUIState()

}