package com.mtp.ticketpartner.scanner.feature_scan_module.feature_login_scan.domain.model

data class LoginWithPinResponse(
    val `data`: Data?,
    val error: Any?,
    val message: String?,
    val status_code: Int?
)

data class Data(
    val access_token: String?,
    val event: EventDetails?,
    val name: String?,
    val scan_key_id: Int?
)

data class EventDetails(
    val city: String?,
    val country: String?,
    val door_close_time: String?,
    val door_open_time: String?,
    val event_cover_image: String?,
    val event_end_date: String?,
    val event_end_time: String?,
    val event_start_date: String?,
    val event_start_time: String?,
    val event_tickets: List<EventTicketList?>?,
    val id: Int?,
    val name: String?,
    val organization_country_name: String?,
    val organization_logo: String?,
    val organization_name: String?,
    val state: String?,
    val is_virtual: Boolean?
)

data class EventTicketList(
    val ticket_name: String?
)