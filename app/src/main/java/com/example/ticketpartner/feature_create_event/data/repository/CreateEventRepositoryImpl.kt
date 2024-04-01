package com.example.ticketpartner.feature_create_event.data.repository

import com.example.ticketpartner.feature_create_event.domain.datasource.CreateEventDataSource
import com.example.ticketpartner.feature_create_event.domain.model.CreateEventGetTimeZoneResponse
import com.example.ticketpartner.feature_create_event.domain.model.CreateEventTicketListResponse
import com.example.ticketpartner.feature_create_event.domain.model.CreateEventTypesResponse
import com.example.ticketpartner.feature_create_event.domain.model.CreateEventVenueStateResponse
import com.example.ticketpartner.feature_create_event.domain.repository.CreateEventRepository
import javax.inject.Inject

class CreateEventRepositoryImpl @Inject constructor(private val createEventDataSource: CreateEventDataSource) :
    CreateEventRepository {
    override suspend fun getTimeZone(): CreateEventGetTimeZoneResponse {
        return createEventDataSource.getTimeZone()
    }

    override suspend fun getEventType(): CreateEventTypesResponse {
        return createEventDataSource.getEventType()
    }

    override suspend fun getStateBasedOnCountry(countryId: Int): CreateEventVenueStateResponse {
        return createEventDataSource.getStateBasedOnCountry(countryId)
    }

    override suspend fun getTicketList(eventId: Int): CreateEventTicketListResponse {
        return createEventDataSource.getTicketList(eventId)
    }
}