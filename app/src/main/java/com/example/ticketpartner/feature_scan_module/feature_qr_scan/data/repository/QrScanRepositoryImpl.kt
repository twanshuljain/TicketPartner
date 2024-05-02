package com.example.ticketpartner.feature_scan_module.feature_qr_scan.data.repository

import com.example.ticketpartner.feature_scan_module.feature_login_scan.domain.model.EventDetailsScanResponse
import com.example.ticketpartner.feature_scan_module.feature_qr_scan.domain.datasource.QrScanDataSource
import com.example.ticketpartner.feature_scan_module.feature_qr_scan.domain.model.QrScanResponse
import com.example.ticketpartner.feature_scan_module.feature_qr_scan.domain.model.QrScannedTicketResponse
import com.example.ticketpartner.feature_scan_module.feature_qr_scan.domain.model.ScanCheckedInResponse
import com.example.ticketpartner.feature_scan_module.feature_qr_scan.domain.model.ScanSearchOrderDetailsResponse
import com.example.ticketpartner.feature_scan_module.feature_qr_scan.domain.model.SearchApiScanResponse
import com.example.ticketpartner.feature_scan_module.feature_qr_scan.domain.repository.QrScanRepository
import javax.inject.Inject

class QrScanRepositoryImpl @Inject constructor(private val qrScanDataSource: QrScanDataSource): QrScanRepository {
    override suspend fun qrScanCode(qrId: String, ticketType: ArrayList<String>): QrScanResponse {
        return qrScanDataSource.qrScanCode(qrId, ticketType)
    }

    override suspend fun getQrScannedTicketData(): QrScannedTicketResponse {
    return  qrScanDataSource.getQrScannedTicketData()
    }

    override suspend fun getScanEventDetails(): EventDetailsScanResponse {
       return qrScanDataSource.getScanEventDetails()
    }

    override suspend fun getScanSearchResponse(orderId: String): SearchApiScanResponse {
       return qrScanDataSource.getScanSearchResponse(orderId)
    }

    override suspend fun getScanOrderDetailsResponse(orderId: String): ScanSearchOrderDetailsResponse {
        return qrScanDataSource.getScanOrderDetailsResponse(orderId)
    }

    override suspend fun getScanCheckedInResponse(
        checkedOrderIdList: ArrayList<Int>,
        orderId: String
    ): ScanCheckedInResponse {
        return qrScanDataSource.getScanCheckedInResponse(checkedOrderIdList, orderId)
    }
}