package com.mtp.ticketpartner.scanner.feature_scan_module.feature_qr_scan.domain.usecase

import com.mtp.ticketpartner.scanner.feature_scan_module.feature_qr_scan.domain.model.QrScanReportAllResponse
import com.mtp.ticketpartner.scanner.feature_scan_module.feature_qr_scan.domain.repository.QrScanRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class GetQrScanReportAllUseCase  @Inject constructor(private val qrScanRepository: QrScanRepository) {
    suspend fun invoke(type: String): Flow<QrScanReportAllResponse> {
        return flow {
            emit(qrScanRepository.getScanReportAllResponse(type))
        }.flowOn(Dispatchers.IO)
    }
}