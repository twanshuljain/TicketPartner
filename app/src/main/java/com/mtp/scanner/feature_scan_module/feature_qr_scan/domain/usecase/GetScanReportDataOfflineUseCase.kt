package com.mtp.scanner.feature_scan_module.feature_qr_scan.domain.usecase

import com.mtp.scanner.feature_local_storage.domain.repository.LocalStorageRepository
import com.mtp.scanner.feature_scan_module.feature_login_scan.domain.model.InsertScanReportDataResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class GetScanReportDataOfflineUseCase @Inject constructor(private val localStorageRepository: LocalStorageRepository) {
    suspend fun invoke(): Flow<List<InsertScanReportDataResponse>> {
        return flow {
            emit(localStorageRepository.getScanReportDataLocalDb())
        }.flowOn(Dispatchers.IO)
    }
}