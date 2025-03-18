package com.mtp.ticketpartner.scanner.feature_login.domain.model

data class VerifyEmailForgotPassRequest(
    val email: String?,
    val otp: String?
)