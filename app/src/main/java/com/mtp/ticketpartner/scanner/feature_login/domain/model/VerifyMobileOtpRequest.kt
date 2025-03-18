package com.mtp.ticketpartner.scanner.feature_login.domain.model

data class VerifyMobileOtpRequest(
    val country_code: String?,
    val mobile_number: String?
)