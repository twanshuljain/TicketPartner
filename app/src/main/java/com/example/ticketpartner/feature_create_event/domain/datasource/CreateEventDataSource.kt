package com.example.ticketpartner.feature_create_event.domain.datasource

import com.example.ticketpartner.feature_create_event.domain.model.CreateEventGetTimeZoneResponse

interface CreateEventDataSource {
    suspend fun getTimeZone(): CreateEventGetTimeZoneResponse
}