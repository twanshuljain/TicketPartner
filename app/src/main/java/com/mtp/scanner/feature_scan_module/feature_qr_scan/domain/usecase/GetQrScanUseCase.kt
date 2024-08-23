package com.mtp.scanner.feature_scan_module.feature_qr_scan.domain.usecase

import com.mtp.scanner.feature_scan_module.feature_qr_scan.domain.model.QrScanResponse
import com.mtp.scanner.feature_scan_module.feature_qr_scan.domain.repository.QrScanRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class GetQrScanUseCase @Inject constructor(private val qrScanRepository: QrScanRepository) {
    suspend fun invoke(qrId: String, ticketType: ArrayList<String>): Flow<QrScanResponse> {
        return flow {
            emit(qrScanRepository.qrScanCode(qrId,ticketType))
        }.flowOn(Dispatchers.IO)
    }
}