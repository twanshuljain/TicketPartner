package com.example.ticketpartner.feature_scan_module.feature_login_scan.domain.usecase

import com.example.ticketpartner.feature_scan_module.feature_login_scan.domain.repository.LoginScanRepository
import com.example.ticketpartner.feature_scan_module.feature_qr_scan.domain.model.QrScanReportAllResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class GetQrScanAllReportLoginUseCase @Inject constructor(private val loginScanRepository: LoginScanRepository) {
    suspend fun invoke(type: String): Flow<QrScanReportAllResponse> {
        return flow { emit(loginScanRepository.getAllScanReportOffline(type)) }.flowOn(Dispatchers.IO)
    }
}