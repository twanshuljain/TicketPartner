package com.mtp.ticketpartner.scanner.feature_local_storage.domain.usecase

import com.mtp.ticketpartner.scanner.feature_local_storage.domain.repository.LocalStorageRepository
import com.mtp.ticketpartner.scanner.feature_scan_module.feature_login_scan.domain.model.DataItems
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class GetQrCodeListFromLocalDBUseCase @Inject constructor(private val localStorageRepository: LocalStorageRepository) {
    suspend fun invoke(): Flow<List<DataItems>> {
        return flow {
            emit(localStorageRepository.getQrCodeListFromLocalDB())
        }.flowOn(Dispatchers.IO)
    }
}