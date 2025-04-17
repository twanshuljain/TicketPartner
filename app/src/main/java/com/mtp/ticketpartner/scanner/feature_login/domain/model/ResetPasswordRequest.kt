package com.mtp.ticketpartner.scanner.feature_login.domain.model

data class ResetPasswordRequest(
    val confirm_password: String?,
    val new_password: String?
)