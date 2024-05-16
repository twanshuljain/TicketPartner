package com.example.ticketpartner.common.localDatabase

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.ticketpartner.common.GET_QR_CODE_LIST_FOR_OFFLINE_SCAN
import com.example.ticketpartner.feature_scan_module.feature_login_scan.domain.model.DataItems

@Dao
interface TpScanDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOfflineScanData(getQrCodeListResponse: DataItems): Long

    @Query("SELECT * FROM $GET_QR_CODE_LIST_FOR_OFFLINE_SCAN")
    suspend fun getQrCodeListFromLocalDB(): List<DataItems>
}