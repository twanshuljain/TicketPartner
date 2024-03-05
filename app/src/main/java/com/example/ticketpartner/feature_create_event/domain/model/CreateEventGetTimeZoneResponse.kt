package com.example.ticketpartner.feature_create_event.domain.model

data class CreateEventGetTimeZoneResponse(
    val `data`: List<Data?>?,
    val error: Any?,
    val message: String?,
    val status_code: Int?
)

data class Data(
    val created_at: String?,
    val created_by: Any?,
    val id: Int?,
    val is_active: Boolean?,
    val time_zone_id: String?,
    val time_zone_name: String?,
    val updated_at: String?,
    val updated_by: Any?
)