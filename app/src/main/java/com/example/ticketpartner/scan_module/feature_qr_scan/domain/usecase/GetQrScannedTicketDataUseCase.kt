package com.example.ticketpartner.scan_module.feature_qr_scan.domain.usecase

import com.example.ticketpartner.scan_module.feature_qr_scan.domain.model.QrScannedTicketResponse
import com.example.ticketpartner.scan_module.feature_qr_scan.domain.repository.QrScanRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class GetQrScannedTicketDataUseCase @Inject constructor(private val qrScanRepository: QrScanRepository) {
    suspend fun invoke():kotlinx.coroutines.flow.Flow<QrScannedTicketResponse>{
        return flow {
            emit(qrScanRepository.getQrScannedTicketData())
        }.flowOn(Dispatchers.IO)
    }
}