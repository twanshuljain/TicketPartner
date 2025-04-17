package com.mtp.ticketpartner.scanner.feature_local_storage.domain.usecase

import com.mtp.ticketpartner.scanner.feature_local_storage.domain.repository.LocalStorageRepository
import com.mtp.ticketpartner.scanner.feature_scan_module.feature_login_scan.domain.model.InsertTicketTypeListResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class InsertTicketTypesOfflineUseCase @Inject constructor(private val localStorageRepository: LocalStorageRepository) {
    suspend fun invoke(ticketName: ArrayList<InsertTicketTypeListResponse>): Flow<List<Long>>{
        return flow {
            emit(localStorageRepository.insertTicketTypesLocalDB(ticketName))
        }.flowOn(Dispatchers.IO)
    }
}