package com.example.ticketpartner.feature_login.domain.model

data class SendSignUpPhoneOtpRequest(
    val country_code: String?,
    val mobile_number: String?
)