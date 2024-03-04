package com.example.ticketpartner.feature_login.domain.model

sealed class ValidationEmailLoginUIState {
    data class IsEmailEmpty(val isEmailEmpty: Boolean ) : ValidationEmailLoginUIState()
    data class EmailIsNotValid(val emailNotValid: String ) : ValidationEmailLoginUIState()
    data class IsPasswordEmpty(val isPasswordEmpty: Boolean ) : ValidationEmailLoginUIState()
    data class OnAllDataValid(val allDataValid: Boolean ) : ValidationEmailLoginUIState()
}