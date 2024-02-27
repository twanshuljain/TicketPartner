package com.example.ticketpartner.feature_add_organization.domain.repository

import com.example.ticketpartner.feature_add_organization.domain.model.AddOrgSocialRequest
import com.example.ticketpartner.feature_add_organization.domain.model.AddOrgSocialResponse
import com.example.ticketpartner.feature_add_organization.domain.model.AddOrganizationResponse

interface AddOrganizationRepository {
    suspend fun addOrganization(
        name: String,
        file: String,
        countryId: String
    ): AddOrganizationResponse

    suspend fun addOrganizationSocial(addOrgSocialRequest: AddOrgSocialRequest): AddOrgSocialResponse
}