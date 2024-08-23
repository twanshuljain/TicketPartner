package com.mtp.scanner.feature_scan_module.feature_login_scan.domain.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.mtp.scanner.common.GET_QR_CODE_LIST_FOR_OFFLINE_SCAN


data class GetQrCodeListResponse(
    val data: List<DataItems?>?,
    val error: Any?,
    val message: String?,
    val status_code: Int?
)

@Entity(tableName = GET_QR_CODE_LIST_FOR_OFFLINE_SCAN)
data class DataItems(
    @ColumnInfo(name = "eventId")
    val event_id: Int?,
    @ColumnInfo(name = "isScanned")
    val is_scanned: Boolean?,
    @ColumnInfo(name = "isServerSync")
    val is_server_sync: Boolean?,
    @ColumnInfo(name = "nameOnTix")
    val name_on_tix: String?,
    @ColumnInfo(name = "offlineId")
    val offline_id: String?,
    @ColumnInfo(name = "orderNumber")
    val order_number: String?,
    @ColumnInfo(name = "orderTicketsId")
    val order_tickets_id: Int?,
    @ColumnInfo(name = "scanDatetime")
    val scan_datetime: String?,
    @ColumnInfo(name = "ticketName")
    val ticket_name: String?,
    @ColumnInfo(name = "ticketPlatformType")
    val ticket_platform_type: String?,
    @ColumnInfo(name = "ticketType")
    val ticket_type: String?,
    @PrimaryKey
    @ColumnInfo(name = "uniqueQrCodeUuid")
    val unique_qrcode_uuid: String
)