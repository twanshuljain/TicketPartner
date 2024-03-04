package com.example.ticketpartner.feature_add_organization.domain.usecase

import com.example.ticketpartner.feature_add_organization.domain.model.AddOrganizationResponse
import com.example.ticketpartner.feature_add_organization.domain.repository.AddOrganizationRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import java.io.File
import javax.inject.Inject

class GetAddOrganizationUseCase @Inject constructor(private val addOrganizationRepository: AddOrganizationRepository) {

    suspend fun invoke(file: File, name: String, countryId: String): Flow<AddOrganizationResponse> {
        return flow {
            emit(addOrganizationRepository.addOrganization(file, name, countryId))
        }.flowOn(Dispatchers.IO)
    }
}