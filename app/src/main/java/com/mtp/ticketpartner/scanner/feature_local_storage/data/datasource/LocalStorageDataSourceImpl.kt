package com.mtp.ticketpartner.scanner.feature_local_storage.data.datasource

import com.mtp.ticketpartner.scanner.common.localDatabase.TpScanDao
import com.mtp.ticketpartner.scanner.feature_local_storage.domain.datasourse.LocalStorageDataSource
import com.mtp.ticketpartner.scanner.feature_scan_module.feature_login_scan.domain.model.CheckInData
import com.mtp.ticketpartner.scanner.feature_scan_module.feature_login_scan.domain.model.DataItems
import com.mtp.ticketpartner.scanner.feature_scan_module.feature_login_scan.domain.model.InsertEventDetailsResponse
import com.mtp.ticketpartner.scanner.feature_scan_module.feature_login_scan.domain.model.InsertScanReportDataResponse
import com.mtp.ticketpartner.scanner.feature_scan_module.feature_login_scan.domain.model.InsertTicketTypeListResponse
import com.mtp.ticketpartner.scanner.feature_scan_module.feature_login_scan.domain.model.SearchData
import com.mtp.ticketpartner.scanner.feature_scan_module.feature_qr_scan.domain.model.ScanLog
import com.mtp.ticketpartner.scanner.feature_scan_module.feature_qr_scan.domain.model.TicketDataList
import javax.inject.Inject

class LocalStorageDataSourceImpl @Inject constructor(private val tpScanDao: TpScanDao) :
    LocalStorageDataSource {
    override suspend fun insertQrCodeList(getQrCodeListResponse: ArrayList<DataItems>): List<Long> {
        return tpScanDao.insertOfflineScanData(getQrCodeListResponse)
    }

    override suspend fun getQrCodeListFromLocalDB(): List<DataItems> {
        return tpScanDao.getQrCodeListFromLocalDB()
    }

    override suspend fun insertEventDetailsLocalDB(insertEventDetailsResponse: InsertEventDetailsResponse): Long {
        return tpScanDao.insertEventDetailsResponse(insertEventDetailsResponse)
    }

    override suspend fun getEventDetailsLocalDB(): InsertEventDetailsResponse {
        return tpScanDao.getEventDetailsFromLocalDB()
    }

    override suspend fun insertTicketTypesLocalDB(ticketName: ArrayList<InsertTicketTypeListResponse>): List<Long> {
        return tpScanDao.insertTicketTypes(ticketName)
    }

    override suspend fun getTicketTypesListLocalDB(): List<InsertTicketTypeListResponse> {
        return tpScanDao.getTicketTypes()
    }

    override suspend fun insertCheckInLocalDB(checkInData: ArrayList<CheckInData>): List<Long> {
        return tpScanDao.insertCheckInList(checkInData)
    }

    override suspend fun getCheckInDataLocalDb(): List<CheckInData> {
        return tpScanDao.getCheckInDataFromLocalDB()
    }

    override suspend fun insertSearchDataLocalDB(searchData: ArrayList<SearchData>): List<Long> {
        return tpScanDao.insertSearchDataList(searchData)
    }

    override suspend fun getSearchDataLocalDb(): List<SearchData> {
        return tpScanDao.getSearchDataFromLocalDB()
    }

    override suspend fun insertScanLogDataLocalDB(scanLog: ScanLog): Long {
        return tpScanDao.insertScanLogDataList(scanLog)
    }

    override suspend fun getScanLogDataLocalDb(): List<ScanLog> {
        return tpScanDao.getScanLogDataFromLocalDB()
    }

    override suspend fun deleteScanLogDataLocalDB(): Int {
        return tpScanDao.deleteScanLogDataFromLocalDB()
    }

    override suspend fun insertScanReportDataLocalDB(insertScanReportDataResponse: InsertScanReportDataResponse): Long {
        return tpScanDao.insertScanReportData(insertScanReportDataResponse)
    }

    override suspend fun insertScanReportTicketListDataLocalDB(ticketDataList: ArrayList<TicketDataList>): List<Long> {
        return tpScanDao.insertScanReportTicketListData(ticketDataList)
    }

    override suspend fun getScanReportDataLocalDb(): List<InsertScanReportDataResponse> {
        return tpScanDao.getScanReportDataFromLocalDB()
    }

    override suspend fun getScanReportTicketListLocalDb(): List<TicketDataList> {
        return tpScanDao.getScanReportTicketListFromLocalDB()
    }
}