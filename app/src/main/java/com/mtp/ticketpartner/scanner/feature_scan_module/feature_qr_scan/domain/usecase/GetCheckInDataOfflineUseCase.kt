package com.mtp.ticketpartner.scanner.feature_scan_module.feature_qr_scan.domain.usecase

import com.mtp.ticketpartner.scanner.feature_local_storage.domain.repository.LocalStorageRepository
import com.mtp.ticketpartner.scanner.feature_scan_module.feature_login_scan.domain.model.CheckInData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class GetCheckInDataOfflineUseCase @Inject constructor(private val localStorageRepository: LocalStorageRepository) {
    suspend fun invoke(): Flow<List<CheckInData>> {
        return flow {
            emit(localStorageRepository.getCheckInDataLocalDb())
        }.flowOn(Dispatchers.IO)
    }
}