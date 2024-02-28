package com.example.ticketpartner.feature_add_organization.domain.repository

import com.example.ticketpartner.feature_add_organization.domain.model.AddOrgSocialRequest
import com.example.ticketpartner.feature_add_organization.domain.model.AddOrgSocialResponse
import com.example.ticketpartner.feature_add_organization.domain.model.AddOrganizationResponse
import java.io.File

interface AddOrganizationRepository {
    suspend fun addOrganization(
        file: File,
        name: String,
        countryId: String
    ): AddOrganizationResponse

    suspend fun addOrganizationSocial(addOrgSocialRequest: AddOrgSocialRequest): AddOrgSocialResponse
}