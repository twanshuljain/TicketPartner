package com.mtp.scanner.feature_add_organization.domain.usecase

import com.mtp.scanner.feature_add_organization.domain.model.SearchCountryResponse
import com.mtp.scanner.feature_add_organization.domain.repository.AddOrganizationRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class GetSearchCountryUseCase @Inject constructor(private val addOrganizationRepository: AddOrganizationRepository){
    suspend fun invoke(): Flow<SearchCountryResponse> {
        return flow {
            emit(addOrganizationRepository.searchCountry())
        }.flowOn(Dispatchers.IO)
    }
}