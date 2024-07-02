package com.example.ticketpartner.common.localDatabase

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.ticketpartner.common.DATABASE_VERSION
import com.example.ticketpartner.feature_scan_module.feature_login_scan.domain.model.CheckInData
import com.example.ticketpartner.feature_scan_module.feature_login_scan.domain.model.DataItems
import com.example.ticketpartner.feature_scan_module.feature_login_scan.domain.model.InsertEventDetailsResponse
import com.example.ticketpartner.feature_scan_module.feature_login_scan.domain.model.InsertScanReportDataResponse
import com.example.ticketpartner.feature_scan_module.feature_login_scan.domain.model.InsertTicketTypeListResponse
import com.example.ticketpartner.feature_scan_module.feature_login_scan.domain.model.SearchData
import com.example.ticketpartner.feature_scan_module.feature_qr_scan.domain.model.ScanLog
import com.example.ticketpartner.feature_scan_module.feature_qr_scan.domain.model.TicketDataList

@Database(
    entities = [DataItems::class, InsertEventDetailsResponse::class,
        InsertTicketTypeListResponse::class, CheckInData::class, SearchData::class, ScanLog::class, InsertScanReportDataResponse::class, TicketDataList::class],
    version = DATABASE_VERSION
)
abstract class TPLocalDatabase : RoomDatabase() {
    abstract fun tpScanDao(): TpScanDao
}