package com.mtp.scanner.feature_scan_module.feature_qr_scan.domain.model

data class QrScanRequest(
    val qrid: String?,
    val ticket_names: List<String?>?
)