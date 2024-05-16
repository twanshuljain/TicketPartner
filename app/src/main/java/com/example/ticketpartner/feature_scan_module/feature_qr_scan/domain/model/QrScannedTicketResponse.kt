package com.example.ticketpartner.feature_scan_module.feature_qr_scan.domain.model


data class QrScannedTicketResponse(
    val data: Data?,
    val error: Any?,
    val message: String?,
    val status_code: Int?
)

data class Data(
    val total_accepted: Int?,
    val total_rejected: Int?,
    val total_scanned: Int?
)