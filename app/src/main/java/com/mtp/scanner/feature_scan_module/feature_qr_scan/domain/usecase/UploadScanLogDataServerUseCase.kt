package com.mtp.scanner.feature_scan_module.feature_qr_scan.domain.usecase

import com.mtp.scanner.feature_scan_module.feature_qr_scan.domain.model.SendScanLogOfflineRequest
import com.mtp.scanner.feature_scan_module.feature_qr_scan.domain.model.UploadScanLogDataServerResponse
import com.mtp.scanner.feature_scan_module.feature_qr_scan.domain.repository.QrScanRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class UploadScanLogDataServerUseCase  @Inject constructor(private val qrScanRepository: QrScanRepository) {
    suspend fun invoke(sendScanLogOfflineResponse: SendScanLogOfflineRequest): Flow<UploadScanLogDataServerResponse> {
        return flow {
            emit(qrScanRepository.getUploadScanLogDataServerResponse(sendScanLogOfflineResponse))
        }.flowOn(Dispatchers.IO)
    }
}