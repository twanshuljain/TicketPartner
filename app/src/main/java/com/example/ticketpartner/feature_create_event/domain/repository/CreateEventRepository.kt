package com.example.ticketpartner.feature_create_event.domain.repository

import com.example.ticketpartner.feature_create_event.domain.model.CreateEventGetTimeZoneResponse
import com.example.ticketpartner.feature_create_event.domain.model.CreateEventTicketListResponse
import com.example.ticketpartner.feature_create_event.domain.model.CreateEventTypesResponse
import com.example.ticketpartner.feature_create_event.domain.model.CreateEventVenueStateResponse

interface CreateEventRepository {
    suspend fun getTimeZone(): CreateEventGetTimeZoneResponse
    suspend fun getEventType(): CreateEventTypesResponse
    suspend fun getStateBasedOnCountry(countryId: Int): CreateEventVenueStateResponse
    suspend fun getTicketList(eventId: Int): CreateEventTicketListResponse
}