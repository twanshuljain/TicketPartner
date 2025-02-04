package com.mtp.scanner.feature_scan_module.feature_login_scan.domain.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.mtp.scanner.common.GET_CHECK_IN_LIST
import com.mtp.scanner.common.ZERO

data class GetCheckInDataOfflineResponse(
    val data: List<CheckInData?>?,
    val error: Any?,
    val message: String?,
    val status_code: Int?
)

@Entity(tableName = GET_CHECK_IN_LIST)
data class CheckInData(
    @ColumnInfo(name = "eventId")
    val event_id: Int?,
    @ColumnInfo(name = "isCheckedIn")
    var is_checked_in: Boolean?,
    @ColumnInfo(name = "isServerSync")
    val is_server_sync: Boolean?,
    @ColumnInfo(name = "nameOnTix")
    val name_on_tix: String?,
    @ColumnInfo(name = "offlineId")
    val offline_id: String?,
    @ColumnInfo(name = "operatorName")
    val operator_name: String?,
    @ColumnInfo(name = "orderNumber")
    val order_number: String?,
    @PrimaryKey
    @ColumnInfo(name = "OrderTicketsId")
    val order_tickets_id: Int = ZERO,
    @ColumnInfo(name = "scanDateTime")
    val scan_datetime: String?,
    @ColumnInfo(name = "scanKeyId")
    val scan_key_id: Int?
)