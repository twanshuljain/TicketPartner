package com.example.ticketpartner.feature_scan_module.feature_login_scan.domain.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.ticketpartner.common.GET_SCAN_REPORT_DATA

@Entity(tableName = GET_SCAN_REPORT_DATA)
data class InsertScanReportDataResponse(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "indexID")
    val indexId: Int,
    @ColumnInfo(name = "online")
    val online: Int,
    @ColumnInfo(name = "physical")
    val physical: Int,
    @ColumnInfo(name = "totalScanned")
    var total_scanned: Int,
    @ColumnInfo(name = "totalAccepted")
    var total_accepted: Int,
    @ColumnInfo(name = "totalRejected")
    var total_rejected: Int,
    @ColumnInfo(name = "totalTickets")
    val total_tickets: Int
)
