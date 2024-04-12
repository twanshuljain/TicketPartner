package com.example.ticketpartner.scan_module.feature_qr_scan.data.repository

import com.example.ticketpartner.scan_module.feature_qr_scan.domain.datasource.QrScanDataSource
import com.example.ticketpartner.scan_module.feature_qr_scan.domain.model.QrScanResponse
import com.example.ticketpartner.scan_module.feature_qr_scan.domain.model.QrScannedTicketResponse
import com.example.ticketpartner.scan_module.feature_qr_scan.domain.repository.QrScanRepository
import javax.inject.Inject

class QrScanRepositoryImpl @Inject constructor(private val qrScanDataSource: QrScanDataSource): QrScanRepository {
    override suspend fun qrScanCode(qrId: String, ticketType: ArrayList<String>): QrScanResponse {
        return qrScanDataSource.qrScanCode(qrId, ticketType)
    }

    override suspend fun getQrScannedTicketData(): QrScannedTicketResponse {
    return  qrScanDataSource.getQrScannedTicketData()
    }
}