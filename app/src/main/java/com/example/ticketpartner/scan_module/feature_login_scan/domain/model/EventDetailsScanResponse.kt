package com.example.ticketpartner.scan_module.feature_login_scan.domain.model

data class EventDetailsScanResponse(
    val data: DataItem?,
    val error: Any?,
    val message: String?,
    val status_code: Int?
)

data class DataItem(
    val event: Event?,
    val event_dates: EventDates?,
    val event_locations: EventLocations?,
    val event_refund_policy: String?,
    val event_tickets: List<EventTicket?>?,
    val event_type: String?,
    val onward_price: Any?,
    val organization: Organization?,
    val user_layout: Any?
)

data class Event(
    val created_at: String?,
    val created_by: Any?,
    val event_additional_cover_images: List<String?>?,
    val event_cover_image: String?,
    val event_description: String?,
    val event_images: List<String?>?,
    val event_status: String?,
    val event_type: EventType?,
    val event_type_id: Int?,
    val event_video: List<Any?>?,
    val id: Int?,
    val is_access_discount_code_created: Boolean?,
    val is_active: Boolean?,
    val is_add_ons_created: Boolean?,
    val is_fee_and_tax_created: Boolean?,
    val is_ticket_created: Boolean?,
    val multi_access_users: Any?,
    val name: String?,
    val organization_id: Int?,
    val past_event_images: List<Any?>?,
    val updated_at: String?,
    val updated_by: Any?,
    val user_id: Int?
)

data class EventDates(
    val created_at: String?,
    val created_by: Any?,
    val door_close_time: String?,
    val door_open_time: String?,
    val event_end_date: String?,
    val event_end_time: String?,
    val event_id: Int?,
    val event_start_date: String?,
    val event_start_time: String?,
    val id: Int?,
    val is_active: Boolean?,
    val is_end_time_show: Boolean?,
    val time_zone_id: Int?,
    val updated_at: String?,
    val updated_by: Any?
)

data class EventLocations(
    val announce_city: Any?,
    val announce_country: Any?,
    val announce_location_name: Any?,
    val announce_state: Any?,
    val announce_street_address: Any?,
    val city: String?,
    val country: String?,
    val created_at: String?,
    val created_by: Any?,
    val event_id: Int?,
    val id: Int?,
    val is_active: Boolean?,
    val is_email: Boolean?,
    val is_to_be_announced: Boolean?,
    val is_venue: Boolean?,
    val is_virtual: Boolean?,
    val latitude: Any?,
    val location_name: String?,
    val longitude: Any?,
    val state: String?,
    val street_address: Any?,
    val updated_at: String?,
    val updated_by: Any?,
    val virtual_event_link: Any?
)

data class EventTicket(
    val advance_setting: Boolean?,
    val created_at: String?,
    val created_by: Any?,
    val donation_amount_type: String?,
    val event_id: Int?,
    val id: Int?,
    val is_access_code: Any?,
    val is_access_code_applied: Any?,
    val is_active: Boolean?,
    val is_allow_to_change_currency: Any?,
    val is_sale_ended: Any?,
    val one_ticket_equal_to: Int?,
    val order_status: String?,
    val ticket_currency_type: String?,
    val ticket_description: String?,
    val ticket_id: Any?,
    val ticket_name: String?,
    val ticket_name_with_type: String?,
    val ticket_per_order_maximum_quantity: Int?,
    val ticket_per_order_minimum_quantity: Int?,
    val ticket_per_user: Boolean?,
    val ticket_per_user_quantity: Int?,
    val ticket_price: Double?,
    val ticket_quantity: Int?,
    val ticket_sale_end_date: String?,
    val ticket_sale_end_time: String?,
    val ticket_sale_start_date: String?,
    val ticket_sale_start_time: String?,
    val ticket_type: String?,
    val ticket_visibility: String?,
    val updated_at: String?,
    val updated_by: Any?,
    val user_id: Int?,
    var isSelected: Boolean = false
)

data class Organization(
    val country_id: Any?,
    val created_at: String?,
    val created_by: Any?,
    val description: Any?,
    val facebook_url: String?,
    val id: Int?,
    val instagram_url: String?,
    val is_active: Boolean?,
    val is_selected: Boolean?,
    val name: String?,
    val organization_logo: String?,
    val subdomain_url: String?,
    val twitter_url: String?,
    val updated_at: String?,
    val updated_by: Any?,
    val user_id: Int?,
    val website_url: String?
)

data class EventType(
    val created_at: String?,
    val created_by: Any?,
    val event_type_title: String?,
    val id: Int?,
    val is_active: Boolean?,
    val updated_at: String?,
    val updated_by: Any?
)