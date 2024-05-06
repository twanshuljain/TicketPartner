package com.example.ticketpartner.feature_scan_module.feature_qr_scan.domain.model

data class ScanCheckedInRequest(
    val order_ids: List<Int?>?,
    val order_number: String?
)