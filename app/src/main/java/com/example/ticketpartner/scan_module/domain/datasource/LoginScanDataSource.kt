package com.example.ticketpartner.scan_module.domain.datasource

import com.example.ticketpartner.scan_module.domain.model.LoginWithPinResponse

interface LoginScanDataSource {
    suspend fun loginScanWithPin(name: String,scanPin: String): LoginWithPinResponse

}