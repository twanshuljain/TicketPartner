package com.mtp.scanner.feature_scan_module.feature_qr_scan.data.repository

import com.mtp.scanner.feature_scan_module.feature_login_scan.domain.model.EventDetailsScanResponse
import com.mtp.scanner.feature_scan_module.feature_login_scan.domain.model.InsertSearchDataResponse
import com.mtp.scanner.feature_scan_module.feature_qr_scan.domain.datasource.QrScanDataSource
import com.mtp.scanner.feature_scan_module.feature_qr_scan.domain.model.QrScanReportAllResponse
import com.mtp.scanner.feature_scan_module.feature_qr_scan.domain.model.QrScanResponse
import com.mtp.scanner.feature_scan_module.feature_qr_scan.domain.model.QrScannedTicketResponse
import com.mtp.scanner.feature_scan_module.feature_qr_scan.domain.model.ScanCheckedInResponse
import com.mtp.scanner.feature_scan_module.feature_qr_scan.domain.model.ScanSearchOrderDetailsResponse
import com.mtp.scanner.feature_scan_module.feature_qr_scan.domain.model.SearchApiScanResponse
import com.mtp.scanner.feature_scan_module.feature_qr_scan.domain.model.SendScanLogOfflineRequest
import com.mtp.scanner.feature_scan_module.feature_qr_scan.domain.model.UploadScanLogDataServerResponse
import com.mtp.scanner.feature_scan_module.feature_qr_scan.domain.repository.QrScanRepository
import javax.inject.Inject

class QrScanRepositoryImpl @Inject constructor(private val qrScanDataSource: QrScanDataSource) :
    QrScanRepository {
    override suspend fun qrScanCode(qrId: String, ticketType: ArrayList<String>): QrScanResponse {
        return qrScanDataSource.qrScanCode(qrId, ticketType)
    }

    override suspend fun getQrScannedTicketData(): QrScannedTicketResponse {
        return qrScanDataSource.getQrScannedTicketData()
    }

    override suspend fun getScanEventDetails(): EventDetailsScanResponse {
        return qrScanDataSource.getScanEventDetails()
    }

    override suspend fun getScanSearchResponse(orderId: String): SearchApiScanResponse {
        return qrScanDataSource.getScanSearchResponse(orderId)
    }

    override suspend fun getScanAllSearchResponse(): InsertSearchDataResponse {
        return qrScanDataSource.getScanAllSearchResponse()
    }

    override suspend fun getScanOrderDetailsResponse(orderId: String): ScanSearchOrderDetailsResponse {
        return qrScanDataSource.getScanOrderDetailsResponse(orderId)
    }

    override suspend fun getScanCheckedInResponse(
        checkedOrderIdList: ArrayList<Long>,
        orderId: String
    ): ScanCheckedInResponse {
        return qrScanDataSource.getScanCheckedInResponse(checkedOrderIdList, orderId)
    }

    override suspend fun getScanReportAllResponse(type: String): QrScanReportAllResponse {
        return qrScanDataSource.getScanReportAllResponse(type)
    }

    override suspend fun getUploadScanLogDataServerResponse(sendScanLogOfflineRequest: SendScanLogOfflineRequest): UploadScanLogDataServerResponse {
        return qrScanDataSource.getUploadScanLogDataServerResponse(sendScanLogOfflineRequest)
    }
}