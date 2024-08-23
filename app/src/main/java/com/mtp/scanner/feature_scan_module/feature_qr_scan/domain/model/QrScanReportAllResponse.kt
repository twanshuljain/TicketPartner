package com.mtp.scanner.feature_scan_module.feature_qr_scan.domain.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.mtp.scanner.common.GET_SCAN_REPORT_TICKET_LIST

data class QrScanReportAllResponse(
    val `data`: DataList?,
    val error: Any?,
    val message: Any?,
    val status_code: Int?
)

data class DataList(
    val online: Int,
    val physical: Int,
    val ticket_data: List<TicketDataList?>?,
    val total_accepted: Int,
    val total_scanned: Int,
    val total_rejected: Int,
    val total_ticket_ratio: TotalTicketRatio,
    val total_tickets: Int
)

@Entity(tableName = GET_SCAN_REPORT_TICKET_LIST)
data class TicketDataList(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "indexId")
    val indexId: Int,
    @ColumnInfo(name = "ratio")
    val ratio: Double?,
    @ColumnInfo(name = "ticketName")
    val ticket_name: String?,
    @ColumnInfo(name = "ticketType")
    val ticket_type: String?,
    @ColumnInfo(name = "totalScanned")
    val total_scanned: Int?,
    @ColumnInfo(name = "totalTicket")
    val total_ticket: Int?
)

data class TotalTicketRatio(
    val accepted_ratio: Double?,
    val online: Double?,
    val physical: Double?,
    val rejected_ratio: Double?,
    val total: Int?
)