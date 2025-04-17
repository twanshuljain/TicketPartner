package com.mtp.ticketpartner.scanner.feature_local_storage.domain.usecase

import com.mtp.ticketpartner.scanner.feature_local_storage.domain.repository.LocalStorageRepository
import com.mtp.ticketpartner.scanner.feature_scan_module.feature_login_scan.domain.model.InsertEventDetailsResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class GetEventDetailsLocalDBUseCase @Inject constructor(private val localStorageRepository: LocalStorageRepository){

    suspend fun invoke(): Flow<InsertEventDetailsResponse> {
        return flow {
            emit(localStorageRepository.getEventDetailsLocalDB())
        }.flowOn(Dispatchers.IO)
    }
}