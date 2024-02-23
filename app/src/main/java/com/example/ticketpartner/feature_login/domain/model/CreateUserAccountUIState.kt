package com.example.ticketpartner.feature_login.domain.model

sealed class CreateUserAccountUIState {
    data class IsLoading(val isLoading: Boolean = false) : CreateUserAccountUIState()
    data class OnSuccess(val result: CreateUserAccountResponse) : CreateUserAccountUIState()
    data class OnFailure(val onFailure: String) : CreateUserAccountUIState()
}