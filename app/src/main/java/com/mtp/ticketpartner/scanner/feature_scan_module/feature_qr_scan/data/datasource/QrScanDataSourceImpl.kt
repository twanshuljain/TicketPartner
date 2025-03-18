package com.mtp.ticketpartner.scanner.feature_scan_module.feature_qr_scan.data.datasource

import com.mtp.ticketpartner.scanner.common.remote.apis.RestApiService
import com.mtp.ticketpartner.scanner.feature_scan_module.feature_login_scan.domain.model.EventDetailsScanResponse
import com.mtp.ticketpartner.scanner.feature_scan_module.feature_login_scan.domain.model.InsertSearchDataResponse
import com.mtp.ticketpartner.scanner.feature_scan_module.feature_qr_scan.domain.datasource.QrScanDataSource
import com.mtp.ticketpartner.scanner.feature_scan_module.feature_qr_scan.domain.model.QrScanReportAllResponse
import com.mtp.ticketpartner.scanner.feature_scan_module.feature_qr_scan.domain.model.QrScanRequest
import com.mtp.ticketpartner.scanner.feature_scan_module.feature_qr_scan.domain.model.QrScanResponse
import com.mtp.ticketpartner.scanner.feature_scan_module.feature_qr_scan.domain.model.QrScannedTicketResponse
import com.mtp.ticketpartner.scanner.feature_scan_module.feature_qr_scan.domain.model.ScanCheckedInRequest
import com.mtp.ticketpartner.scanner.feature_scan_module.feature_qr_scan.domain.model.ScanCheckedInResponse
import com.mtp.ticketpartner.scanner.feature_scan_module.feature_qr_scan.domain.model.ScanSearchOrderDetailsResponse
import com.mtp.ticketpartner.scanner.feature_scan_module.feature_qr_scan.domain.model.SearchApiScanResponse
import com.mtp.ticketpartner.scanner.feature_scan_module.feature_qr_scan.domain.model.SendScanLogOfflineRequest
import com.mtp.ticketpartner.scanner.feature_scan_module.feature_qr_scan.domain.model.UploadScanLogDataServerResponse
import javax.inject.Inject

class QrScanDataSourceImpl @Inject constructor(private val restApiService: RestApiService) :
    QrScanDataSource {
    override suspend fun qrScanCode(qrId: String, ticketType: ArrayList<String>): QrScanResponse {
        return restApiService.scanQrCode(QrScanRequest(qrid = qrId, ticket_names = ticketType))
    }

    override suspend fun getQrScannedTicketData(): QrScannedTicketResponse {
        return restApiService.getQrScannedTicketData()
    }

    override suspend fun getScanEventDetails(): EventDetailsScanResponse {
        return restApiService.getScanEventDetails()
    }

    override suspend fun getScanSearchResponse(orderId: String): SearchApiScanResponse {
        return restApiService.getQrScannedSearchData(orderId = orderId)
    }

    override suspend fun getScanAllSearchResponse(): InsertSearchDataResponse {
        return restApiService.getQrScannedSearchAllDataOffline()
    }

    override suspend fun getScanOrderDetailsResponse(orderId: String): ScanSearchOrderDetailsResponse {
        return restApiService.getQrScanOrderDetailsData(orderId)
    }

    override suspend fun getScanCheckedInResponse(
        checkedOrderIdList: ArrayList<Long>,
        orderId: String
    ): ScanCheckedInResponse {
        return restApiService.getQrScanCheckedInData(
            ScanCheckedInRequest(
                checkedOrderIdList,
                orderId
            )
        )
    }

    override suspend fun getScanReportAllResponse(type: String): QrScanReportAllResponse {
        return restApiService.getQrScanReportAllData(type)
    }

    override suspend fun getUploadScanLogDataServerResponse(sendScanLogOfflineRequest: SendScanLogOfflineRequest): UploadScanLogDataServerResponse {
        return restApiService.uploadScanLogDataServer(sendScanLogOfflineRequest)
    }
}