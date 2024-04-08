package com.example.ticketpartner.scan_module.domain.usecase

import com.example.ticketpartner.scan_module.domain.model.EventDetailsScanResponse
import com.example.ticketpartner.scan_module.domain.repository.LoginScanRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class GetScanEventDetailsUseCase @Inject constructor(private val loginScanRepository: LoginScanRepository) {
    suspend fun invoke(): kotlinx.coroutines.flow.Flow<EventDetailsScanResponse> {
        return flow {
            emit(loginScanRepository.getScanEventDetails())
        }.flowOn(Dispatchers.IO)
    }
}