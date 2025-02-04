package com.mtp.scanner.feature_login.domain.model

data class VerifyEmailForgotPassRequest(
    val email: String?,
    val otp: String?
)