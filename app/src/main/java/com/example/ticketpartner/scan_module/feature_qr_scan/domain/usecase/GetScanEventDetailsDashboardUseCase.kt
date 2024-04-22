package com.example.ticketpartner.scan_module.feature_qr_scan.domain.usecase

import com.example.ticketpartner.scan_module.feature_login_scan.domain.model.EventDetailsScanResponse
import com.example.ticketpartner.scan_module.feature_qr_scan.domain.repository.QrScanRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class GetScanEventDetailsDashboardUseCase @Inject constructor(private val qrScanRepository: QrScanRepository) {
    suspend fun invoke(): kotlinx.coroutines.flow.Flow<EventDetailsScanResponse> {
        return flow {
            emit(qrScanRepository.getScanEventDetails())
        }.flowOn(Dispatchers.IO)
    }
}