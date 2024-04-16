package com.example.ticketpartner.scan_module.feature_qr_scan.domain.datasource

import com.example.ticketpartner.scan_module.feature_qr_scan.domain.model.QrScanResponse
import com.example.ticketpartner.scan_module.feature_qr_scan.domain.model.QrScannedTicketResponse

interface QrScanDataSource {
    suspend fun qrScanCode(qrId: String,ticketType: ArrayList<String>): QrScanResponse
    suspend fun getQrScannedTicketData(): QrScannedTicketResponse
}