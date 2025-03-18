package com.mtp.ticketpartner.scanner.feature_scan_module.feature_qr_scan.domain.model

data class QRScanDetails(
    val customerName: String,
    val isTransfer: Boolean,
    val isRefunded: Boolean,
    val isValidQr: Boolean,
    val isTransferredTo: String,
    val isValid: Boolean,
)
