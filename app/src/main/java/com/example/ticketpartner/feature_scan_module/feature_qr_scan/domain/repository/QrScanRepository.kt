package com.example.ticketpartner.feature_scan_module.feature_qr_scan.domain.repository

import com.example.ticketpartner.feature_scan_module.feature_login_scan.domain.model.EventDetailsScanResponse
import com.example.ticketpartner.feature_scan_module.feature_qr_scan.domain.model.QrScanResponse
import com.example.ticketpartner.feature_scan_module.feature_qr_scan.domain.model.QrScannedTicketResponse
import com.example.ticketpartner.feature_scan_module.feature_qr_scan.domain.model.ScanCheckedInResponse
import com.example.ticketpartner.feature_scan_module.feature_qr_scan.domain.model.ScanSearchOrderDetailsResponse
import com.example.ticketpartner.feature_scan_module.feature_qr_scan.domain.model.SearchApiScanResponse

interface QrScanRepository {
    suspend fun qrScanCode(qrId: String,ticketType: ArrayList<String>): QrScanResponse
    suspend fun getQrScannedTicketData(): QrScannedTicketResponse
    suspend fun getScanEventDetails(): EventDetailsScanResponse
    suspend fun getScanSearchResponse(orderId: String): SearchApiScanResponse
    suspend fun getScanOrderDetailsResponse(orderId: String): ScanSearchOrderDetailsResponse
    suspend fun getScanCheckedInResponse(checkedOrderIdList: ArrayList<Int>, orderId: String): ScanCheckedInResponse
}