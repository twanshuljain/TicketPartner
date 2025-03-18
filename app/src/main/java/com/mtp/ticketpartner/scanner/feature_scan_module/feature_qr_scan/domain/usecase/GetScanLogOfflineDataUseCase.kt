package com.mtp.ticketpartner.scanner.feature_scan_module.feature_qr_scan.domain.usecase

import com.mtp.ticketpartner.scanner.feature_local_storage.domain.repository.LocalStorageRepository
import com.mtp.ticketpartner.scanner.feature_scan_module.feature_qr_scan.domain.model.ScanLog
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class GetScanLogOfflineDataUseCase @Inject constructor(private val localStorageRepository: LocalStorageRepository) {
    suspend fun invoke(): Flow<List<ScanLog>> {
        return flow {
            emit(localStorageRepository.getScanLogDataLocalDb())
        }.flowOn(Dispatchers.IO)
    }
}