package com.example.ticketpartner.scan_module.domain.model

data class LoginWithPinRequest(
    val name: String?,
    val scan_pin: String?
)