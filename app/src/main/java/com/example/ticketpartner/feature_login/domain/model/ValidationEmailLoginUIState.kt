package com.example.ticketpartner.feature_login.domain.model

sealed class ValidationEmailLoginUIState {

    /**
     *  State to update UI
     */
    data class Loading(val isLoading: Boolean) : ValidationEmailLoginUIState()

    /**
     *  State to handle email validation
     */

    /**
     *  State to handle all data validation
     */
    data class IsPasswordEmpty(val emailNotValid: String ) : ValidationEmailLoginUIState()
    data class OnAllDataValid(val emailNotValid: Boolean ) : ValidationEmailLoginUIState()
    data class EmailIsNotValid(val emailNotValid: String ) : ValidationEmailLoginUIState()
    data class AllFieldsRequired(val emailNotValid: String ) : ValidationEmailLoginUIState()

    /**
     *  State to handle email validation
     */
    data class OnEmailEnter(val isValid: Boolean) : ValidationEmailLoginUIState()
}