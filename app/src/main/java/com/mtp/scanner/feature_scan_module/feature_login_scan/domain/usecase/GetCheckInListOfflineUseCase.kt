package com.mtp.scanner.feature_scan_module.feature_login_scan.domain.usecase

import com.mtp.scanner.feature_scan_module.feature_login_scan.domain.model.GetCheckInDataOfflineResponse
import com.mtp.scanner.feature_scan_module.feature_login_scan.domain.repository.LoginScanRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class GetCheckInListOfflineUseCase @Inject constructor(private val loginScanRepository: LoginScanRepository) {
    suspend fun invoke():Flow<GetCheckInDataOfflineResponse>{
        return flow {
            emit(loginScanRepository.getCheckInListOffline())
        }.flowOn(Dispatchers.IO)
    }
}