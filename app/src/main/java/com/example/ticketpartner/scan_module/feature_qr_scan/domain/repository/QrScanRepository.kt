package com.example.ticketpartner.scan_module.feature_qr_scan.domain.repository

import com.example.ticketpartner.scan_module.feature_login_scan.domain.model.EventDetailsScanResponse
import com.example.ticketpartner.scan_module.feature_qr_scan.domain.model.QrScanResponse
import com.example.ticketpartner.scan_module.feature_qr_scan.domain.model.QrScannedTicketResponse

interface QrScanRepository {
    suspend fun qrScanCode(qrId: String,ticketType: ArrayList<String>): QrScanResponse
    suspend fun getQrScannedTicketData(): QrScannedTicketResponse
    suspend fun getScanEventDetails(): EventDetailsScanResponse
}