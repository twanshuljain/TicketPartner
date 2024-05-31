package com.example.ticketpartner.feature_scan_module.feature_login_scan.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.ticketpartner.common.GET_CHECK_IN_LIST_FOR_QR_SCAN


data class GetCheckInDataOfflineScanResponse(
    val data: List<DataCheckIn?>?,
    val error: Any?,
    val message: String?,
    val status_code: Int?
)

@Entity(tableName = GET_CHECK_IN_LIST_FOR_QR_SCAN)
data class DataCheckIn(
    val event_id: Int?,
    val is_checked_in: Boolean?,
    val is_server_sync: Boolean?,
    val name_on_tix: String?,
    val offline_id: String?,
    val operator_name: String?,
    @PrimaryKey
    val order_number: String?,
    val order_tickets_id: Int?,
    val scan_datetime: String?,
    val scan_key_id: Int?
)