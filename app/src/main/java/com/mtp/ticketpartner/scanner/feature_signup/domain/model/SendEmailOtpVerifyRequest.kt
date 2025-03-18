package com.mtp.ticketpartner.scanner.feature_signup.domain.model

data class SendEmailOtpVerifyRequest(
    val email: String?,
    val otp: String?
)