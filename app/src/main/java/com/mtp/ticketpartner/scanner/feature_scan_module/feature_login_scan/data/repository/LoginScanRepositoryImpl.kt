package com.mtp.ticketpartner.scanner.feature_scan_module.feature_login_scan.data.repository

import com.mtp.ticketpartner.scanner.feature_scan_module.feature_login_scan.domain.datasource.LoginScanDataSource
import com.mtp.ticketpartner.scanner.feature_scan_module.feature_login_scan.domain.model.EventDetailsScanResponse
import com.mtp.ticketpartner.scanner.feature_scan_module.feature_login_scan.domain.model.GetCheckInDataOfflineResponse
import com.mtp.ticketpartner.scanner.feature_scan_module.feature_login_scan.domain.model.GetQrCodeListResponse
import com.mtp.ticketpartner.scanner.feature_scan_module.feature_login_scan.domain.model.LoginWithPinResponse
import com.mtp.ticketpartner.scanner.feature_scan_module.feature_login_scan.domain.repository.LoginScanRepository
import com.mtp.ticketpartner.scanner.feature_scan_module.feature_qr_scan.domain.model.QrScanReportAllResponse
import javax.inject.Inject

class LoginScanRepositoryImpl @Inject constructor(private val loginScanDataSource: LoginScanDataSource) :
    LoginScanRepository {
    override suspend fun loginScanWithPin(name: String, scanPin: String): LoginWithPinResponse {
        return loginScanDataSource.loginScanWithPin(name, scanPin)
    }

    override suspend fun getScanEventDetails(): EventDetailsScanResponse {
        return loginScanDataSource.getScanEventDetails()
    }

    override suspend fun getQrCodeListForOfflineScan(): GetQrCodeListResponse {
        return loginScanDataSource.getQrCodeListForOfflineScan()
    }

    override suspend fun getCheckInListOffline(): GetCheckInDataOfflineResponse {
        return loginScanDataSource.getCheckInListOffline()
    }

    override suspend fun getAllScanReportOffline(type: String): QrScanReportAllResponse {
        return loginScanDataSource.getAllScanReportOffline(type)
    }
}