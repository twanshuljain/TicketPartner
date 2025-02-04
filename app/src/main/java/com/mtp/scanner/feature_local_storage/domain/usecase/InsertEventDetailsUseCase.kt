package com.mtp.scanner.feature_local_storage.domain.usecase

import com.mtp.scanner.feature_local_storage.domain.repository.LocalStorageRepository
import com.mtp.scanner.feature_scan_module.feature_login_scan.domain.model.InsertEventDetailsResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class InsertEventDetailsUseCase @Inject constructor(private val repository: LocalStorageRepository) {
    suspend fun invoke(insertEventDetailsResponse: InsertEventDetailsResponse): Flow<Long> {
        return flow {
            emit(repository.insertEventDetailsLocalDB(insertEventDetailsResponse))
        }.flowOn(Dispatchers.IO)
    }
}