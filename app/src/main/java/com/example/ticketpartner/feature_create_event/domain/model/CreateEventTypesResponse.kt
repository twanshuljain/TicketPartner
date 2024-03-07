package com.example.ticketpartner.feature_create_event.domain.model

data class CreateEventTypesResponse(
    val data: List<DataItem?>?,
    val error: Any?,
    val message: String?,
    val status_code: Int?
)

data class DataItem(
    val created_at: String?,
    val created_by: Any?,
    val event_type_title: String?,
    val id: Int?,
    val is_active: Boolean?,
    val updated_at: String?,
    val updated_by: Any?
)