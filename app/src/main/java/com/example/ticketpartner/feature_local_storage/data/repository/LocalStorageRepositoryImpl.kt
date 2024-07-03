package com.example.ticketpartner.feature_local_storage.data.repository

import com.example.ticketpartner.feature_local_storage.domain.datasourse.LocalStorageDataSource
import com.example.ticketpartner.feature_local_storage.domain.repository.LocalStorageRepository
import com.example.ticketpartner.feature_scan_module.feature_login_scan.domain.model.CheckInData
import com.example.ticketpartner.feature_scan_module.feature_login_scan.domain.model.DataItems
import com.example.ticketpartner.feature_scan_module.feature_login_scan.domain.model.InsertEventDetailsResponse
import com.example.ticketpartner.feature_scan_module.feature_login_scan.domain.model.InsertScanReportDataResponse
import com.example.ticketpartner.feature_scan_module.feature_login_scan.domain.model.InsertTicketTypeListResponse
import com.example.ticketpartner.feature_scan_module.feature_login_scan.domain.model.SearchData
import com.example.ticketpartner.feature_scan_module.feature_qr_scan.domain.model.ScanLog
import com.example.ticketpartner.feature_scan_module.feature_qr_scan.domain.model.TicketDataList
import javax.inject.Inject

class LocalStorageRepositoryImpl @Inject constructor(private val localStorageDataSource: LocalStorageDataSource) :
    LocalStorageRepository {
    override suspend fun insertQrCodeList(getQrCodeListResponse: ArrayList<DataItems>): List<Long> {
        return localStorageDataSource.insertQrCodeList(getQrCodeListResponse)
    }

    override suspend fun getQrCodeListFromLocalDB(): List<DataItems> {
        return localStorageDataSource.getQrCodeListFromLocalDB()
    }

    override suspend fun insertEventDetailsLocalDB(insertEventDetailsResponse: InsertEventDetailsResponse): Long {
        return localStorageDataSource.insertEventDetailsLocalDB(insertEventDetailsResponse)

    }

    override suspend fun getEventDetailsLocalDB(): InsertEventDetailsResponse {
        return localStorageDataSource.getEventDetailsLocalDB()
    }

    override suspend fun insertTicketTypesLocalDB(ticketName: ArrayList<InsertTicketTypeListResponse>): List<Long> {
        return localStorageDataSource.insertTicketTypesLocalDB(ticketName)
    }

    override suspend fun getTicketTypesListLocalDB(): List<InsertTicketTypeListResponse> {
        return localStorageDataSource.getTicketTypesListLocalDB()
    }

    override suspend fun insertCheckInLocalDB(checkInData: ArrayList<CheckInData>): List<Long> {
        return localStorageDataSource.insertCheckInLocalDB(checkInData)
    }

    override suspend fun getCheckInDataLocalDb(): List<CheckInData> {
        return localStorageDataSource.getCheckInDataLocalDb()
    }

    override suspend fun insertSearchDataLocalDB(searchData: ArrayList<SearchData>): List<Long> {
        return localStorageDataSource.insertSearchDataLocalDB(searchData)
    }

    override suspend fun getSearchDataLocalDb(): List<SearchData> {
        return localStorageDataSource.getSearchDataLocalDb()
    }

    override suspend fun insertScanLogDataLocalDB(scanLog: ScanLog): Long {
        return localStorageDataSource.insertScanLogDataLocalDB(scanLog)
    }

    override suspend fun getScanLogDataLocalDb(): List<ScanLog> {
        return localStorageDataSource.getScanLogDataLocalDb()
    }

    override suspend fun deleteScanLogDataLocalDB(): Int {
        return localStorageDataSource.deleteScanLogDataLocalDB()
    }

    override suspend fun insertScanReportDataLocalDB(insertScanReportDataResponse: InsertScanReportDataResponse): Long {
        return localStorageDataSource.insertScanReportDataLocalDB(insertScanReportDataResponse)
    }

    override suspend fun insertScanReportTicketListDataLocalDB(ticketDataList: ArrayList<TicketDataList>): List<Long> {
        return localStorageDataSource.insertScanReportTicketListDataLocalDB(ticketDataList)
    }

    override suspend fun getScanReportDataLocalDb(): List<InsertScanReportDataResponse> {
        return localStorageDataSource.getScanReportDataLocalDb()
    }

    override suspend fun getScanReportTicketListLocalDb(): List<TicketDataList> {
        return localStorageDataSource.getScanReportTicketListLocalDb()
    }
}