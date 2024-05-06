package com.example.ticketpartner.feature_scan_module.feature_qr_scan.domain.model

data class ScanCheckedInResponse(
    val `data`: Items?,
    val error: Any?,
    val message: String?,
    val status_code: Int?
)

data class Items(
    val order_ids: List<Int?>?,
    val order_number: String?
)