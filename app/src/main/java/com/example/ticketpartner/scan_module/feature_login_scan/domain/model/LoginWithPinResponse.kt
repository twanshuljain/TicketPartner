package com.example.ticketpartner.scan_module.feature_login_scan.domain.model

data class LoginWithPinResponse(
    val data: Data?,
    val error: Any?,
    val message: String?,
    val status_code: Int?
)

data class Data(
    val access_token: String?,
    val name: String?
)