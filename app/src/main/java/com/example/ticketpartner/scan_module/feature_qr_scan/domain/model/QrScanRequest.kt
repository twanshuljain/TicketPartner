package com.example.ticketpartner.scan_module.feature_qr_scan.domain.model

data class QrScanRequest(
    val qrid: String?,
    val ticket_name: List<String?>?
)