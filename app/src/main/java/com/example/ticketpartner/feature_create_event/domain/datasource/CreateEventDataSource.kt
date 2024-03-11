package com.example.ticketpartner.feature_create_event.domain.datasource

import com.example.ticketpartner.feature_create_event.domain.model.CreateEventGetTimeZoneResponse
import com.example.ticketpartner.feature_create_event.domain.model.CreateEventTypesResponse
import com.example.ticketpartner.feature_create_event.domain.model.CreateEventVenueStateResponse

interface CreateEventDataSource {
    suspend fun getTimeZone(): CreateEventGetTimeZoneResponse
    suspend fun getEventType(): CreateEventTypesResponse
    suspend fun getStateBasedOnCountry(countryId: Int): CreateEventVenueStateResponse
}