package com.mtp.ticketpartner.scanner.feature_signup.domain.model

data class SendPhoneSignUpOtpVerifyRequest(
    val country_code: String?,
    val mobile_number: String?,
    val otp: String?
)