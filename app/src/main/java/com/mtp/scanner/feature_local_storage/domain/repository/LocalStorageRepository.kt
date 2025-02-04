package com.mtp.scanner.feature_local_storage.domain.repository

import com.mtp.scanner.feature_scan_module.feature_login_scan.domain.model.CheckInData
import com.mtp.scanner.feature_scan_module.feature_login_scan.domain.model.DataItems
import com.mtp.scanner.feature_scan_module.feature_login_scan.domain.model.InsertEventDetailsResponse
import com.mtp.scanner.feature_scan_module.feature_login_scan.domain.model.InsertScanReportDataResponse
import com.mtp.scanner.feature_scan_module.feature_login_scan.domain.model.InsertTicketTypeListResponse
import com.mtp.scanner.feature_scan_module.feature_login_scan.domain.model.SearchData
import com.mtp.scanner.feature_scan_module.feature_qr_scan.domain.model.ScanLog
import com.mtp.scanner.feature_scan_module.feature_qr_scan.domain.model.TicketDataList

interface LocalStorageRepository {
    suspend fun insertQrCodeList(getQrCodeListResponse: ArrayList<DataItems>): List<Long>
    suspend fun getQrCodeListFromLocalDB(): List<DataItems>
    suspend fun insertEventDetailsLocalDB(insertEventDetailsResponse: InsertEventDetailsResponse): Long
    suspend fun getEventDetailsLocalDB(): InsertEventDetailsResponse
    suspend fun insertTicketTypesLocalDB(ticketName: ArrayList<InsertTicketTypeListResponse>): List<Long>
    suspend fun getTicketTypesListLocalDB(): List<InsertTicketTypeListResponse>
    suspend fun insertCheckInLocalDB(checkInData: ArrayList<CheckInData>): List<Long>
    suspend fun getCheckInDataLocalDb(): List<CheckInData>
    suspend fun insertSearchDataLocalDB(searchData: ArrayList<SearchData>): List<Long>
    suspend fun getSearchDataLocalDb(): List<SearchData>
    suspend fun insertScanLogDataLocalDB(scanLog: ScanLog): Long
    suspend fun getScanLogDataLocalDb(): List<ScanLog>
    suspend fun deleteScanLogDataLocalDB(): Int
    suspend fun insertScanReportDataLocalDB(insertScanReportDataResponse: InsertScanReportDataResponse): Long
    suspend fun insertScanReportTicketListDataLocalDB(ticketDataList: ArrayList<TicketDataList>): List<Long>
    suspend fun getScanReportDataLocalDb(): List<InsertScanReportDataResponse>
    suspend fun getScanReportTicketListLocalDb(): List<TicketDataList>
}