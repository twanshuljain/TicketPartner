package com.mtp.scanner.feature_scan_module.feature_qr_scan.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.mtp.scanner.common.EMPTY_STRING
import com.mtp.scanner.common.GET_SCAN_LOG_LIST
import com.mtp.scanner.common.ZERO

data class SendScanLogOfflineRequest(
    val data: List<ScanLog?>?
)
@Entity(tableName = GET_SCAN_LOG_LIST)
data class ScanLog(
   /* @PrimaryKey(autoGenerate = true)
    val uniqueId: Int = 0,*/
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