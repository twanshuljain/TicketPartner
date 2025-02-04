package com.mtp.scanner.feature_local_storage.domain.usecase

import com.mtp.scanner.feature_local_storage.domain.repository.LocalStorageRepository
import com.mtp.scanner.feature_scan_module.feature_login_scan.domain.model.DataItems
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class InsertQrCodeListUseCase @Inject constructor(private val localStorageRepository: LocalStorageRepository){
    suspend fun invoke(getQrCodeListResponse: ArrayList<DataItems>): Flow<List<Long>> {
        return flow { emit(localStorageRepository.insertQrCodeList(getQrCodeListResponse)) }.flowOn(Dispatchers.IO)
    }
}