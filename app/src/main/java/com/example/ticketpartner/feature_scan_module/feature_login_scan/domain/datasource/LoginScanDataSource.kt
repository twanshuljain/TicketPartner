package com.example.ticketpartner.feature_scan_module.feature_login_scan.domain.datasource

import com.example.ticketpartner.feature_scan_module.feature_login_scan.domain.model.EventDetailsScanResponse
import com.example.ticketpartner.feature_scan_module.feature_login_scan.domain.model.LoginWithPinResponse

interface LoginScanDataSource {
    suspend fun loginScanWithPin(name: String,scanPin: String): LoginWithPinResponse

    suspend fun getScanEventDetails(): EventDetailsScanResponse

}