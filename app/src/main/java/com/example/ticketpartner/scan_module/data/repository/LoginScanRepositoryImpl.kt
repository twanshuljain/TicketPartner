package com.example.ticketpartner.scan_module.data.repository

import com.example.ticketpartner.scan_module.domain.datasource.LoginScanDataSource
import com.example.ticketpartner.scan_module.domain.model.LoginWithPinResponse
import com.example.ticketpartner.scan_module.domain.repository.LoginScanRepository
import javax.inject.Inject

class LoginScanRepositoryImpl @Inject constructor(private val loginScanDataSource: LoginScanDataSource) :
    LoginScanRepository {
    override suspend fun loginScanWithPin(name: String, scanPin: String): LoginWithPinResponse {
        return loginScanDataSource.loginScanWithPin(name, scanPin)
    }
}