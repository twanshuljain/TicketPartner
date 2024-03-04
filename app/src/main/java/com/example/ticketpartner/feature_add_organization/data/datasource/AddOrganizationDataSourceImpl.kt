package com.example.ticketpartner.feature_add_organization.data.datasource

import com.example.ticketpartner.common.FILE
import com.example.ticketpartner.common.remote.apis.COUNTRY_ID
import com.example.ticketpartner.common.remote.apis.NAME
import com.example.ticketpartner.common.remote.apis.RestApiService
import com.example.ticketpartner.feature_add_organization.domain.datasource.AddOrganizationDataSource
import com.example.ticketpartner.feature_add_organization.domain.model.AddOrgSocialRequest
import com.example.ticketpartner.feature_add_organization.domain.model.AddOrgSocialResponse
import com.example.ticketpartner.feature_add_organization.domain.model.AddOrganizationResponse
import com.example.ticketpartner.feature_add_organization.domain.model.SearchCountryResponse
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import javax.inject.Inject

class AddOrganizationDataSourceImpl @Inject constructor(private val restApiService: RestApiService) :
    AddOrganizationDataSource {
    override suspend fun addOrganization(
        file: File,
        name: String,
        countryId: String
    ): AddOrganizationResponse {

        var body: MultipartBody.Part? = null
        file?.let {
            val requestFile: RequestBody = RequestBody.create(
                "*/*".toMediaType(),
                file
            )
            body = MultipartBody.Part.createFormData(FILE, file.name, requestFile)
        }
        val textData = HashMap<String, RequestBody>()
        textData[NAME] = name.toRequestBody("text/plain".toMediaTypeOrNull())
        textData[COUNTRY_ID] = countryId.toRequestBody("text/plain".toMediaTypeOrNull())

        return restApiService.addOrganization(body, textData)
    }

    override suspend fun addOrganizationSocial(addOrgSocialRequest: AddOrgSocialRequest): AddOrgSocialResponse {
        return restApiService.addOrganizationSocial(addOrgSocialRequest)
    }

    override suspend fun searchCountry(): SearchCountryResponse {
        return restApiService.searchCountry()
    }
}