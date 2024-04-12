package com.example.ticketpartner.scan_module.feature_login_scan.domain.model

data class LoginWithPinRequest(
    val name: String?,
    val scan_pin: String?
)