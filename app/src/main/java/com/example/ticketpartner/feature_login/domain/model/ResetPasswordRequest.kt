package com.example.ticketpartner.feature_login.domain.model

data class ResetPasswordRequest(
    val confirm_password: String?,
    val new_password: String?
)