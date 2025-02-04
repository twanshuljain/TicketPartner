package com.mtp.scanner.feature_signup.domain.model

data class SendEmailOtpVerifyRequest(
    val email: String?,
    val otp: String?
)