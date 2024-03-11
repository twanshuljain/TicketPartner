package com.example.ticketpartner.feature_create_event.domain.model

data class CreateEventVenueStateResponse(
    val data: List<DataState?>?,
    val error: Any?,
    val message: String?,
    val status_code: Int?
)

data class DataState(
    val abbreviation: String?,
    val country_id: Int?,
    val country_name: String?,
    val created_at: String?,
    val created_by: Any?,
    val id: Int?,
    val is_active: Boolean?,
    val state_code: String?,
    val state_name: String?,
    val updated_at: String?,
    val updated_by: Any?
)