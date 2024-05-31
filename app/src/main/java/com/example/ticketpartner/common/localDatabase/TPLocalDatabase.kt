package com.example.ticketpartner.common.localDatabase

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.ticketpartner.common.DATABASE_VERSION
import com.example.ticketpartner.feature_scan_module.feature_login_scan.domain.model.DataItems
import com.example.ticketpartner.feature_scan_module.feature_login_scan.domain.model.InsertEventDetailsResponse
import com.example.ticketpartner.feature_scan_module.feature_login_scan.domain.model.InsertTicketTypeListResponse

@Database(entities = [DataItems::class,InsertEventDetailsResponse::class,InsertTicketTypeListResponse::class], version = DATABASE_VERSION)
abstract  class TPLocalDatabase: RoomDatabase() {
    abstract fun tpScanDao(): TpScanDao
}