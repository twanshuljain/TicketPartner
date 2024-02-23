package com.example.ticketpartner.feature_login.domain.model

data class SendEmailOtpVerifyRequest(
    val email: String?,
    val otp: String?
)