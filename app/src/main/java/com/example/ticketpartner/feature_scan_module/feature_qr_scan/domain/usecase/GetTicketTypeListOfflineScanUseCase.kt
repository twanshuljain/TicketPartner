package com.example.ticketpartner.feature_scan_module.feature_qr_scan.domain.usecase

import com.example.ticketpartner.feature_local_storage.domain.repository.LocalStorageRepository
import com.example.ticketpartner.feature_scan_module.feature_login_scan.domain.model.InsertTicketTypeListResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class GetTicketTypeListOfflineScanUseCase @Inject constructor(private val localStorageRepository: LocalStorageRepository) {
    fun invoke(): Flow<List<InsertTicketTypeListResponse>> {
        return flow {
            emit(localStorageRepository.getTicketTypesListLocalDB())
        }.flowOn(Dispatchers.IO)
    }
}