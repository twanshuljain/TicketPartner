package com.mtp.ticketpartner.scanner.common.localDatabase

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.mtp.ticketpartner.scanner.common.GET_CHECK_IN_LIST
import com.mtp.ticketpartner.scanner.common.GET_EVENT_DETAILS
import com.mtp.ticketpartner.scanner.common.GET_QR_CODE_LIST_FOR_OFFLINE_SCAN
import com.mtp.ticketpartner.scanner.common.GET_SCAN_LOG_LIST
import com.mtp.ticketpartner.scanner.common.GET_SCAN_REPORT_DATA
import com.mtp.ticketpartner.scanner.common.GET_SCAN_REPORT_TICKET_LIST
import com.mtp.ticketpartner.scanner.common.GET_SEARCH_LIST
import com.mtp.ticketpartner.scanner.common.GET_TICKET_TYPES_LIST
import com.mtp.ticketpartner.scanner.feature_scan_module.feature_login_scan.domain.model.CheckInData
import com.mtp.ticketpartner.scanner.feature_scan_module.feature_login_scan.domain.model.DataItems
import com.mtp.ticketpartner.scanner.feature_scan_module.feature_login_scan.domain.model.InsertEventDetailsResponse
import com.mtp.ticketpartner.scanner.feature_scan_module.feature_login_scan.domain.model.InsertScanReportDataResponse
import com.mtp.ticketpartner.scanner.feature_scan_module.feature_login_scan.domain.model.InsertTicketTypeListResponse
import com.mtp.ticketpartner.scanner.feature_scan_module.feature_login_scan.domain.model.SearchData
import com.mtp.ticketpartner.scanner.feature_scan_module.feature_qr_scan.domain.model.ScanLog
import com.mtp.ticketpartner.scanner.feature_scan_module.feature_qr_scan.domain.model.TicketDataList

@Dao
interface TpScanDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOfflineScanData(getQrCodeListResponse: ArrayList<DataItems>): List<Long>

    @Query("SELECT * FROM $GET_QR_CODE_LIST_FOR_OFFLINE_SCAN")
    suspend fun getQrCodeListFromLocalDB(): List<DataItems>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEventDetailsResponse(insertEventDetailsResponse: InsertEventDetailsResponse): Long

    @Query("SELECT * FROM $GET_EVENT_DETAILS")
    suspend fun getEventDetailsFromLocalDB(): InsertEventDetailsResponse

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTicketTypes(ticketName: ArrayList<InsertTicketTypeListResponse>): List<Long>

    @Query("SELECT * FROM $GET_TICKET_TYPES_LIST")
    suspend fun getTicketTypes(): List<InsertTicketTypeListResponse>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCheckInList(checkInData: ArrayList<CheckInData>): List<Long>

    @Query("SELECT * FROM $GET_CHECK_IN_LIST")
    suspend fun getCheckInDataFromLocalDB(): List<CheckInData>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSearchDataList(searchData: ArrayList<SearchData>): List<Long>

    @Query("SELECT * FROM $GET_SEARCH_LIST")
    suspend fun getSearchDataFromLocalDB(): List<SearchData>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertScanLogDataList(scanLog: ScanLog): Long

    @Query("SELECT * FROM $GET_SCAN_LOG_LIST")
    suspend fun getScanLogDataFromLocalDB(): List<ScanLog>

    @Query("DELETE FROM $GET_SCAN_LOG_LIST")
    suspend fun deleteScanLogDataFromLocalDB(): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertScanReportData(insertScanReportDataResponse: InsertScanReportDataResponse): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertScanReportTicketListData(ticketDataList: ArrayList<TicketDataList>): List<Long>

    @Query("SELECT * FROM $GET_SCAN_REPORT_DATA")
    suspend fun getScanReportDataFromLocalDB(): List<InsertScanReportDataResponse>

    @Query("SELECT * FROM $GET_SCAN_REPORT_TICKET_LIST")
    suspend fun getScanReportTicketListFromLocalDB(): List<TicketDataList>

}