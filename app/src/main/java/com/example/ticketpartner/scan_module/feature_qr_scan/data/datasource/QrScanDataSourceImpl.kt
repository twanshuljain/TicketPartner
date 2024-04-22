package com.example.ticketpartner.scan_module.feature_qr_scan.data.datasource

import com.example.ticketpartner.common.remote.apis.RestApiService
import com.example.ticketpartner.scan_module.feature_login_scan.domain.model.EventDetailsScanResponse
import com.example.ticketpartner.scan_module.feature_qr_scan.domain.datasource.QrScanDataSource
import com.example.ticketpartner.scan_module.feature_qr_scan.domain.model.QrScanRequest
import com.example.ticketpartner.scan_module.feature_qr_scan.domain.model.QrScanResponse
import com.example.ticketpartner.scan_module.feature_qr_scan.domain.model.QrScannedTicketResponse
import javax.inject.Inject

class QrScanDataSourceImpl @Inject constructor(private val restApiService: RestApiService): QrScanDataSource {
    override suspend fun qrScanCode(qrId: String, ticketType: ArrayList<String>): QrScanResponse {
        return restApiService.scanQrCode(QrScanRequest(qrid = qrId, ticket_name = ticketType))
    }

    override suspend fun getQrScannedTicketData(): QrScannedTicketResponse {
       return restApiService.getQrScannedTicketData()
    }

    override suspend fun getScanEventDetails(): EventDetailsScanResponse {
        return restApiService.getScanEventDetails()
    }
}