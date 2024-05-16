package com.example.ticketpartner.feature_scan_module.feature_login_scan.domain.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.ticketpartner.common.GET_QR_CODE_LIST_FOR_OFFLINE_SCAN


data class GetQrCodeListResponse(
    val data: List<DataItems?>?,
    val error: Any?,
    val message: String?,
    val status_code: Int?
)

@Entity(tableName = GET_QR_CODE_LIST_FOR_OFFLINE_SCAN)
data class DataItems(
    @ColumnInfo(name = "event_id")
    val event_id: Int?,
    @ColumnInfo(name = "is_scanned")
    val is_scanned: Boolean?,
    @ColumnInfo(name = "is_server_sync")
    val is_server_sync: Boolean?,
    @ColumnInfo(name = "name_on_tix")
    val name_on_tix: String?,
    @ColumnInfo(name = "offline_id")
    val offline_id: String?,
    @ColumnInfo(name = "order_number")
    val order_number: String?,
    @ColumnInfo(name = "order_tickets_id")
    val order_tickets_id: Int?,
    @ColumnInfo(name = "scan_datetime")
    val scan_datetime: String?,
    @ColumnInfo(name = "ticket_name")
    val ticket_name: String?,
    @ColumnInfo(name = "ticket_platform_type")
    val ticket_platform_type: String?,
    @ColumnInfo(name = "ticket_type")
    val ticket_type: String?,
    @PrimaryKey
    @ColumnInfo(name = "unique_qrcode_uuid")
    val unique_qrcode_uuid: String
)