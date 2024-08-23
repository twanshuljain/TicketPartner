package com.mtp.scanner.feature_scan_module.feature_login_scan.domain.usecase

import com.mtp.scanner.feature_local_storage.domain.repository.LocalStorageRepository
import com.mtp.scanner.feature_scan_module.feature_qr_scan.domain.model.TicketDataList
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class InsertScanReportTicketListUSeCase @Inject constructor(private val repository: LocalStorageRepository) {
    suspend fun invoke(ticketDataList: ArrayList<TicketDataList>): Flow<List<Long>> {
        return flow {
            emit(repository.insertScanReportTicketListDataLocalDB(ticketDataList))
        }.flowOn(Dispatchers.IO)
    }
}