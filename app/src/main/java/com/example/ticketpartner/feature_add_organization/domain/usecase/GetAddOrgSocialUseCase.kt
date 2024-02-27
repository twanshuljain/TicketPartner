package com.example.ticketpartner.feature_add_organization.domain.usecase

import com.example.ticketpartner.feature_add_organization.domain.model.AddOrgSocialRequest
import com.example.ticketpartner.feature_add_organization.domain.model.AddOrgSocialResponse
import com.example.ticketpartner.feature_add_organization.domain.repository.AddOrganizationRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class GetAddOrgSocialUseCase @Inject constructor(private val addOrganizationRepository: AddOrganizationRepository) {
   suspend fun invoke(addOrgSocialRequest: AddOrgSocialRequest): Flow<AddOrgSocialResponse>{
       return flow {
           emit(addOrganizationRepository.addOrganizationSocial(addOrgSocialRequest))
       }.flowOn(Dispatchers.IO)
   }
}