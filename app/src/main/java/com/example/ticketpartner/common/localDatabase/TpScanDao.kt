package com.example.ticketpartner.common.localDatabase

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.ticketpartner.common.GET_EVENT_DETAILS
import com.example.ticketpartner.common.GET_QR_CODE_LIST_FOR_OFFLINE_SCAN
import com.example.ticketpartner.common.GET_TICKET_TYPES_LIST
import com.example.ticketpartner.feature_scan_module.feature_login_scan.domain.model.DataItems
import com.example.ticketpartner.feature_scan_module.feature_login_scan.domain.model.InsertEventDetailsResponse
import com.example.ticketpartner.feature_scan_module.feature_login_scan.domain.model.InsertTicketTypeListResponse

@Dao
interface TpScanDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOfflineScanData(getQrCodeListResponse: DataItems): Long

    @Query("SELECT * FROM $GET_QR_CODE_LIST_FOR_OFFLINE_SCAN")
    suspend fun getQrCodeListFromLocalDB(): List<DataItems>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEventDetailsResponse(insertEventDetailsResponse: InsertEventDetailsResponse): Long

    @Query("SELECT * FROM $GET_EVENT_DETAILS")
    suspend fun getEventDetailsFromLocalDB(): InsertEventDetailsResponse

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTicketTypes(ticketName: InsertTicketTypeListResponse): Long

    @Query("SELECT * FROM $GET_TICKET_TYPES_LIST")
    suspend fun getTicketTypesListFromLocalDB(): List<InsertTicketTypeListResponse>
}