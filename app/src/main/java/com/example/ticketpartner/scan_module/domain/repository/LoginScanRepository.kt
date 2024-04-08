package com.example.ticketpartner.scan_module.domain.repository

import com.example.ticketpartner.scan_module.domain.model.EventDetailsScanResponse
import com.example.ticketpartner.scan_module.domain.model.LoginWithPinResponse

interface LoginScanRepository {
    suspend fun loginScanWithPin(name: String,scanPin: String): LoginWithPinResponse
    suspend fun getScanEventDetails(): EventDetailsScanResponse
}