package com.example.ticketpartner.scan_module.feature_login_scan.domain.repository

import com.example.ticketpartner.scan_module.feature_login_scan.domain.model.EventDetailsScanResponse
import com.example.ticketpartner.scan_module.feature_login_scan.domain.model.LoginWithPinResponse

interface LoginScanRepository {
    suspend fun loginScanWithPin(name: String,scanPin: String): LoginWithPinResponse
    suspend fun getScanEventDetails(): EventDetailsScanResponse
}