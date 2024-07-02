package com.example.ticketpartner.feature_scan_module.feature_login_scan.domain.usecase

import com.example.ticketpartner.feature_local_storage.domain.repository.LocalStorageRepository
import com.example.ticketpartner.feature_scan_module.feature_login_scan.domain.model.InsertScanReportDataResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class InsertScanReportDataUseCase @Inject constructor(private val repository: LocalStorageRepository) {
    suspend fun invoke(insertScanReportDataResponse: InsertScanReportDataResponse): Flow<Long> {
        return flow {
            emit(repository.insertScanReportDataLocalDB(insertScanReportDataResponse))
        }.flowOn(Dispatchers.IO)
    }
}