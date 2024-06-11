package com.example.ticketpartner.feature_scan_module.feature_qr_scan.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.ticketpartner.common.EMPTY_STRING
import com.example.ticketpartner.common.GET_SCAN_LOG_LIST
import com.example.ticketpartner.common.ZERO

data class SendScanLogOfflineRequest(
    val data: List<ScanLog?>?
)
@Entity(tableName = GET_SCAN_LOG_LIST)
data class ScanLog(
    val errorDetail: String? = EMPTY_STRING,
    val eventID: Int? = ZERO,
    val isServerSync: Boolean? = false,
    val nameOnTix: String? = EMPTY_STRING,
    val offlineID: String? = EMPTY_STRING,
    val operatorName: String? = EMPTY_STRING,
    val order: String? = EMPTY_STRING,
    @PrimaryKey(autoGenerate = false)
    val orderTicketsID: Int? = ZERO,
    val qrCode: String? = EMPTY_STRING,
    val scanDatetime: String? = EMPTY_STRING,
    val scanKeyID: Int? = ZERO,
    val status: Boolean? = false,
    val ticketName: String? = EMPTY_STRING,
    val ticketPlatformType: String? = EMPTY_STRING,
    val ticketType: String? = EMPTY_STRING
)