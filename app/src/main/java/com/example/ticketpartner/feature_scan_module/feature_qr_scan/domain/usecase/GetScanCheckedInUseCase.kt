package com.example.ticketpartner.feature_scan_module.feature_qr_scan.domain.usecase

import com.example.ticketpartner.feature_scan_module.feature_qr_scan.domain.model.ScanCheckedInResponse
import com.example.ticketpartner.feature_scan_module.feature_qr_scan.domain.repository.QrScanRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class GetScanCheckedInUseCase @Inject constructor(private val qrScanRepository: QrScanRepository) {
    suspend fun invoke(checkedOrderIdList:ArrayList<Int>,orderId: String): Flow<ScanCheckedInResponse> {
        return flow {
            emit(qrScanRepository.getScanCheckedInResponse(checkedOrderIdList,orderId))
        }.flowOn(Dispatchers.IO)
    }
}