package com.example.ticketpartner.feature_scan_module.feature_login_scan.data.datasource

import com.example.ticketpartner.common.remote.apis.RestApiService
import com.example.ticketpartner.feature_scan_module.feature_login_scan.domain.datasource.LoginScanDataSource
import com.example.ticketpartner.feature_scan_module.feature_login_scan.domain.model.EventDetailsScanResponse
import com.example.ticketpartner.feature_scan_module.feature_login_scan.domain.model.GetCheckInDataOfflineResponse
import com.example.ticketpartner.feature_scan_module.feature_login_scan.domain.model.GetQrCodeListResponse
import com.example.ticketpartner.feature_scan_module.feature_login_scan.domain.model.LoginWithPinRequest
import com.example.ticketpartner.feature_scan_module.feature_login_scan.domain.model.LoginWithPinResponse
import javax.inject.Inject

class LoginScanDataSourceImpl @Inject constructor(private val restApiService: RestApiService) :
    LoginScanDataSource {
    override suspend fun loginScanWithPin(name: String, scanPin: String): LoginWithPinResponse {
        return restApiService.loginWithPin(LoginWithPinRequest(name = name, scan_pin = scanPin))
    }

    override suspend fun getScanEventDetails(): EventDetailsScanResponse {
     return restApiService.getScanEventDetails()
    }

    override suspend fun getQrCodeListForOfflineScan(): GetQrCodeListResponse {
        return restApiService.getQrListForOfflineScan()
    }

    override suspend fun getCheckInListOffline(): GetCheckInDataOfflineResponse {
        return restApiService.getCheckInListForOffline()
    }
}