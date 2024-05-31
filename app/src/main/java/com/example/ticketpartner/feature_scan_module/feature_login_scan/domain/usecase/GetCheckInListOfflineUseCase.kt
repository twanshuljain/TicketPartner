package com.example.ticketpartner.feature_scan_module.feature_login_scan.domain.usecase

import com.example.ticketpartner.feature_scan_module.feature_login_scan.domain.model.GetCheckInDataOfflineScanResponse
import com.example.ticketpartner.feature_scan_module.feature_login_scan.domain.repository.LoginScanRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class GetCheckInListOfflineUseCase @Inject constructor(private val loginScanRepository: LoginScanRepository) {
    suspend fun invoke(): Flow<GetCheckInDataOfflineScanResponse>{
        return flow { emit(loginScanRepository.getCheckInDataForOfflineScan()) }.flowOn(Dispatchers.IO)
    }
}