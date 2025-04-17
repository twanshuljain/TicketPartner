package com.mtp.ticketpartner.scanner.feature_scan_module.feature_qr_scan.domain.datasource

import com.mtp.ticketpartner.scanner.feature_scan_module.feature_login_scan.domain.model.EventDetailsScanResponse
import com.mtp.ticketpartner.scanner.feature_scan_module.feature_login_scan.domain.model.InsertSearchDataResponse
import com.mtp.ticketpartner.scanner.feature_scan_module.feature_qr_scan.domain.model.QrScanReportAllResponse
import com.mtp.ticketpartner.scanner.feature_scan_module.feature_qr_scan.domain.model.QrScanResponse
import com.mtp.ticketpartner.scanner.feature_scan_module.feature_qr_scan.domain.model.QrScannedTicketResponse
import com.mtp.ticketpartner.scanner.feature_scan_module.feature_qr_scan.domain.model.ScanCheckedInResponse
import com.mtp.ticketpartner.scanner.feature_scan_module.feature_qr_scan.domain.model.ScanSearchOrderDetailsResponse
import com.mtp.ticketpartner.scanner.feature_scan_module.feature_qr_scan.domain.model.SearchApiScanResponse
import com.mtp.ticketpartner.scanner.feature_scan_module.feature_qr_scan.domain.model.SendScanLogOfflineRequest
import com.mtp.ticketpartner.scanner.feature_scan_module.feature_qr_scan.domain.model.UploadScanLogDataServerResponse

interface QrScanDataSource {
    suspend fun qrScanCode(qrId: String, ticketType: ArrayList<String>): QrScanResponse
    suspend fun getQrScannedTicketData(): QrScannedTicketResponse
    suspend fun getScanEventDetails(): EventDetailsScanResponse
    suspend fun getScanSearchResponse(orderId: String): SearchApiScanResponse
    suspend fun getScanAllSearchResponse(): InsertSearchDataResponse
    suspend fun getScanOrderDetailsResponse(orderId: String): ScanSearchOrderDetailsResponse
    suspend fun getScanCheckedInResponse(
        checkedOrderIdList: ArrayList<Long>,
        orderId: String
    ): ScanCheckedInResponse

    suspend fun getScanReportAllResponse(type: String): QrScanReportAllResponse
    suspend fun getUploadScanLogDataServerResponse(sendScanLogOfflineRequest: SendScanLogOfflineRequest): UploadScanLogDataServerResponse

}