package com.mtp.scanner.feature_scan_module.feature_login_scan.domain.usecase

import com.mtp.scanner.feature_scan_module.feature_login_scan.domain.model.LoginWithPinResponse
import com.mtp.scanner.feature_scan_module.feature_login_scan.domain.repository.LoginScanRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class GetLoginWithPinUseCase @Inject constructor(private val loginScanRepository: LoginScanRepository) {
    suspend fun invoke(
        name: String,
        scanPin: String
    ): kotlinx.coroutines.flow.Flow<LoginWithPinResponse> {
        return flow {
            emit(loginScanRepository.loginScanWithPin(name, scanPin))
        }.flowOn(Dispatchers.IO)
    }

}