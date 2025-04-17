package com.mtp.ticketpartner.scanner.feature_scan_module.feature_qr_scan.domain.model

data class ScanCheckedInRequest(
    val order_ids: List<Long?>?,
    val order_number: String?
)