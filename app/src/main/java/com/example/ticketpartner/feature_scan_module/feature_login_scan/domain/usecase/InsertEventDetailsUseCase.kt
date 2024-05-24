package com.example.ticketpartner.feature_scan_module.feature_login_scan.domain.usecase

import com.example.ticketpartner.feature_local_storage.domain.repository.LocalStorageRepository
import com.example.ticketpartner.feature_scan_module.feature_login_scan.domain.model.InsertEventDetailsResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class InsertEventDetailsUseCase @Inject constructor(private val repository: LocalStorageRepository) {
    suspend fun invoke(insertEventDetailsResponse: InsertEventDetailsResponse): Flow<Long> {
        return flow {
            emit(repository.insertEventDetailsLocalDB(insertEventDetailsResponse))
        }
    }
}