package com.mtp.ticketpartner.scanner.feature_add_organization.domain.datasource

import com.mtp.ticketpartner.scanner.feature_add_organization.domain.model.AddOrgSocialRequest
import com.mtp.ticketpartner.scanner.feature_add_organization.domain.model.AddOrgSocialResponse
import com.mtp.ticketpartner.scanner.feature_add_organization.domain.model.AddOrganizationResponse
import com.mtp.ticketpartner.scanner.feature_add_organization.domain.model.SearchCountryResponse
import java.io.File

interface AddOrganizationDataSource {
    suspend fun addOrganization(
        file: File,
        name: String,
        countryId: String
    ): AddOrganizationResponse

    suspend fun addOrganizationSocial(addOrgSocialRequest: AddOrgSocialRequest): AddOrgSocialResponse
    suspend fun searchCountry(): SearchCountryResponse
}