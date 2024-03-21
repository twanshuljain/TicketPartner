package com.example.ticketpartner.feature_create_event.domain.model

data class CreateEventTicketListResponse(
    val data: List<DataModel?>?,
    val error: Any?,
    val message: String?,
    val status_code: Int?
)

data class DataModel(
    val advance_setting: Any?,
    val created_at: String?,
    val created_by: Any?,
    val event_id: Int?,
    val id: Int?,
    val is_active: Boolean?,
    val order_status: String?,
    val sold_ticket: Int?,
    val ticket_description: String?,
    val ticket_id: Any?,
    val ticket_name: String?,
    val ticket_name_with_type: String?,
    val ticket_per_order_maximum_quantity: Any?,
    val ticket_per_order_minimum_quantity: Any?,
    val ticket_per_user: Any?,
    val ticket_per_user_quantity: Any?,
    val ticket_quantity: Int?,
    val ticket_sale_end_date: String?,
    val ticket_sale_end_time: String?,
    val ticket_sale_start_date: String?,
    val ticket_sale_start_time: String?,
    val ticket_type: String?,
    val ticket_visibility: String?,
    val updated_at: String?,
    val updated_by: Any?,
    val user_id: Int?
)