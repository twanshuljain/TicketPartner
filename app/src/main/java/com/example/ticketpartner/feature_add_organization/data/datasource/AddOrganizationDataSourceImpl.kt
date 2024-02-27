package com.example.ticketpartner.feature_add_organization.data.datasource

import com.example.ticketpartner.common.remote.apis.RestApiService
import com.example.ticketpartner.feature_add_organization.domain.datasource.AddOrganizationDataSource
import com.example.ticketpartner.feature_add_organization.domain.model.AddOrgSocialRequest
import com.example.ticketpartner.feature_add_organization.domain.model.AddOrgSocialResponse
import com.example.ticketpartner.feature_add_organization.domain.model.AddOrganizationResponse
import javax.inject.Inject

class AddOrganizationDataSourceImpl @Inject constructor(private val restApiService: RestApiService) :
    AddOrganizationDataSource {
    override suspend fun addOrganization(
        name: String,
        file: String,
        countryId: String
    ): AddOrganizationResponse {
        return restApiService.addOrganization()
    }

    override suspend fun addOrganizationSocial(addOrgSocialRequest: AddOrgSocialRequest): AddOrgSocialResponse {
        return restApiService.addOrganizationSocial(addOrgSocialRequest)
    }
}