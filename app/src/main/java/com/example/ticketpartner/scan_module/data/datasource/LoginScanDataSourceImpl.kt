package com.example.ticketpartner.scan_module.data.datasource

import com.example.ticketpartner.common.remote.apis.RestApiService
import com.example.ticketpartner.scan_module.domain.datasource.LoginScanDataSource
import com.example.ticketpartner.scan_module.domain.model.LoginWithPinRequest
import com.example.ticketpartner.scan_module.domain.model.LoginWithPinResponse
import javax.inject.Inject

class LoginScanDataSourceImpl @Inject constructor(private val restApiService: RestApiService) :
    LoginScanDataSource {
    override suspend fun loginScanWithPin(name: String, scanPin: String): LoginWithPinResponse {
        return restApiService.loginWithPin(LoginWithPinRequest(name = name, scan_pin = scanPin))
    }
}