package com.example.ticketpartner.feature_scan_module.feature_login_scan.domain.model

data class GetCheckInDataOfflineResponse(
    //val `data`: List<Data?>?,
    val error: Any?,
    val message: String?,
    val status_code: Int?
)

/*
data class Data(
    val event_id: Int?,
    val is_checked_in: Boolean?,
    val is_server_sync: Boolean?,
    val name_on_tix: String?,
    val offline_id: String?,
    val operator_name: String?,
    val order_number: String?,
    @PrimaryKey
    val order_tickets_id: Int?,
    val scan_datetime: String?,
    val scan_key_id: Int?
)*/
