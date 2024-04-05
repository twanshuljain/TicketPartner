package com.example.ticketpartner.scan_module.domain.usecase

import com.example.ticketpartner.scan_module.domain.model.LoginWithPinResponse
import com.example.ticketpartner.scan_module.domain.repository.LoginScanRepository
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