package com.example.ticketpartner.feature_scan_module.feature_qr_scan.domain.model

data class QrErrorResponse(
    val `data`: Error?,
    val error: Any?,
    val message: String?,
    val status_code: Int?
)

data class Error(
    val customer_name: String?
)