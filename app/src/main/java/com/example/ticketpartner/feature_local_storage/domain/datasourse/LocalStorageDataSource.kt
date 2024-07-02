package com.example.ticketpartner.feature_local_storage.domain.datasourse

import com.example.ticketpartner.feature_scan_module.feature_login_scan.domain.model.CheckInData
import com.example.ticketpartner.feature_scan_module.feature_login_scan.domain.model.DataItems
import com.example.ticketpartner.feature_scan_module.feature_login_scan.domain.model.InsertEventDetailsResponse
import com.example.ticketpartner.feature_scan_module.feature_login_scan.domain.model.InsertScanReportDataResponse
import com.example.ticketpartner.feature_scan_module.feature_login_scan.domain.model.InsertTicketTypeListResponse
import com.example.ticketpartner.feature_scan_module.feature_login_scan.domain.model.SearchData
import com.example.ticketpartner.feature_scan_module.feature_qr_scan.domain.model.ScanLog
import com.example.ticketpartner.feature_scan_module.feature_qr_scan.domain.model.TicketDataList

interface LocalStorageDataSource {
    suspend fun insertQrCodeList(getQrCodeListResponse: DataItems): Long
    suspend fun getQrCodeListFromLocalDB(): List<DataItems>
    suspend fun insertEventDetailsLocalDB(insertEventDetailsResponse: InsertEventDetailsResponse): Long
    suspend fun getEventDetailsLocalDB(): InsertEventDetailsResponse
    suspend fun insertTicketTypesLocalDB(ticketName: InsertTicketTypeListResponse): Long
    suspend fun getTicketTypesListLocalDB(): List<InsertTicketTypeListResponse>
    suspend fun insertCheckInLocalDB(checkInData: CheckInData): Long
    suspend fun getCheckInDataLocalDb(): List<CheckInData>
    suspend fun insertSearchDataLocalDB(searchData: SearchData): Long
    suspend fun getSearchDataLocalDb(): List<SearchData>
    suspend fun insertScanLogDataLocalDB(scanLog: ScanLog): Long
    suspend fun getScanLogDataLocalDb(): List<ScanLog>
    suspend fun deleteScanLogDataLocalDB(): Int
    suspend fun insertScanReportDataLocalDB(insertScanReportDataResponse: InsertScanReportDataResponse): Long
    suspend fun insertScanReportTicketListDataLocalDB(ticketDataList: TicketDataList): Long
    suspend fun getScanReportDataLocalDb(): List<InsertScanReportDataResponse>
    suspend fun getScanReportTicketListLocalDb(): List<TicketDataList>

}