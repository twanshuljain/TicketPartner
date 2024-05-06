package com.example.ticketpartner.feature_scan_module.feature_qr_scan.domain.usecase

import com.example.ticketpartner.feature_scan_module.feature_qr_scan.domain.model.ScanSearchOrderDetailsResponse
import com.example.ticketpartner.feature_scan_module.feature_qr_scan.domain.repository.QrScanRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class GetScanOrderDetailsUseCase  @Inject constructor(private val qrScanRepository: QrScanRepository) {
    suspend fun invoke(orderId:String): Flow<ScanSearchOrderDetailsResponse> {
        return flow {
            emit(qrScanRepository.getScanOrderDetailsResponse(orderId))
        }.flowOn(Dispatchers.IO)
    }
}