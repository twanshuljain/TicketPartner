package com.example.ticketpartner.feature_signup.domain.model

data class SendEmailOtpVerifyRequest(
    val email: String?,
    val otp: String?
)