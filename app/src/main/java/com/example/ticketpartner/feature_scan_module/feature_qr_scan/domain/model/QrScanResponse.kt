package com.example.ticketpartner.feature_scan_module.feature_qr_scan.domain.model

data class QrScanResponse(
    val data: Any?,
    val error: Any?,
    val message: String?,
    val status_code: Int?
)