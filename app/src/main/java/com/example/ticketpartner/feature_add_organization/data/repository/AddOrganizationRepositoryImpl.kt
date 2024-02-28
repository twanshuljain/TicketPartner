package com.example.ticketpartner.feature_add_organization.data.repository

import com.example.ticketpartner.feature_add_organization.domain.datasource.AddOrganizationDataSource
import com.example.ticketpartner.feature_add_organization.domain.model.AddOrgSocialRequest
import com.example.ticketpartner.feature_add_organization.domain.model.AddOrgSocialResponse
import com.example.ticketpartner.feature_add_organization.domain.model.AddOrganizationResponse
import com.example.ticketpartner.feature_add_organization.domain.repository.AddOrganizationRepository
import java.io.File
import javax.inject.Inject

class AddOrganizationRepositoryImpl @Inject constructor(private val addOrganizationDataSource: AddOrganizationDataSource) :
    AddOrganizationRepository {
    override suspend fun addOrganization(
        file: File,
        name: String,
        countryId: String
    ): AddOrganizationResponse {
        return addOrganizationDataSource.addOrganization(file,name,countryId)
    }

    override suspend fun addOrganizationSocial(addOrgSocialRequest: AddOrgSocialRequest): AddOrgSocialResponse {
        return addOrganizationDataSource.addOrganizationSocial(addOrgSocialRequest)
    }
}