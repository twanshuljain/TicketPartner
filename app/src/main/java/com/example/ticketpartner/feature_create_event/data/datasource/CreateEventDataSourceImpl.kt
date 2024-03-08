package com.example.ticketpartner.feature_create_event.data.datasource

import com.example.ticketpartner.common.remote.apis.RestApiService
import com.example.ticketpartner.feature_create_event.domain.datasource.CreateEventDataSource
import com.example.ticketpartner.feature_create_event.domain.model.CreateEventGetTimeZoneResponse
import com.example.ticketpartner.feature_create_event.domain.model.CreateEventTypesResponse
import com.example.ticketpartner.feature_create_event.domain.model.CreateEventVenueStateResponse
import javax.inject.Inject

class CreateEventDataSourceImpl @Inject constructor(private val restApiService: RestApiService) :
    CreateEventDataSource {
    override suspend fun getTimeZone(): CreateEventGetTimeZoneResponse {
        return restApiService.getTimeZone()
    }

    override suspend fun getEventType(): CreateEventTypesResponse {
       return restApiService.getEventType()
    }

    override suspend fun getStateBasedOnCountry(countryId: Int): CreateEventVenueStateResponse {
        return restApiService.getStateBasedOnCountry(countryId)
    }
}