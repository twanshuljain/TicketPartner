package com.mtp.scanner.feature_scan_module.feature_login_scan.domain.usecase

import com.mtp.scanner.feature_local_storage.domain.repository.LocalStorageRepository
import com.mtp.scanner.feature_scan_module.feature_login_scan.domain.model.CheckInData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class InsertCheckInDataOfflineUseCase @Inject constructor(private val localStorageRepository: LocalStorageRepository) {
    suspend fun invoke(checkInData: ArrayList<CheckInData>): Flow<List<Long>>{
        return flow {
            emit(localStorageRepository.insertCheckInLocalDB(checkInData))
        }.flowOn(Dispatchers.IO)
    }
}