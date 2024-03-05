package com.example.ticketpartner.feature_create_event.domain.repository

import com.example.ticketpartner.feature_create_event.domain.model.CreateEventGetTimeZoneResponse

interface CreateEventRepository {
    suspend fun getTimeZone(): CreateEventGetTimeZoneResponse
}