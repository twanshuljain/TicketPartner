package com.mtp.scanner.feature_scan_module.feature_login_scan.domain.repository

import com.mtp.scanner.feature_scan_module.feature_login_scan.domain.model.EventDetailsScanResponse
import com.mtp.scanner.feature_scan_module.feature_login_scan.domain.model.GetCheckInDataOfflineResponse
import com.mtp.scanner.feature_scan_module.feature_login_scan.domain.model.GetQrCodeListResponse
import com.mtp.scanner.feature_scan_module.feature_login_scan.domain.model.LoginWithPinResponse
import com.mtp.scanner.feature_scan_module.feature_qr_scan.domain.model.QrScanReportAllResponse

interface LoginScanRepository {
    suspend fun loginScanWithPin(name: String,scanPin: String): LoginWithPinResponse
    suspend fun getScanEventDetails(): EventDetailsScanResponse
    suspend fun getQrCodeListForOfflineScan(): GetQrCodeListResponse
    suspend fun getCheckInListOffline(): GetCheckInDataOfflineResponse
    suspend fun getAllScanReportOffline(type: String): QrScanReportAllResponse
}