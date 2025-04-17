package com.mtp.ticketpartner.scanner.feature_login.domain.model

data class UserLoginPhoneRequest(
    val country_code: String?,
    val mobile_number: String?,
    val otp: String?
)