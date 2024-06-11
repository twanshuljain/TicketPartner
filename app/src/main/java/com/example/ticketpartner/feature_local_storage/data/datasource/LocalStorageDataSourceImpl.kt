package com.example.ticketpartner.feature_local_storage.data.datasource

import com.example.ticketpartner.common.localDatabase.TpScanDao
import com.example.ticketpartner.feature_local_storage.domain.datasourse.LocalStorageDataSource
import com.example.ticketpartner.feature_scan_module.feature_login_scan.domain.model.CheckInData
import com.example.ticketpartner.feature_scan_module.feature_login_scan.domain.model.DataItems
import com.example.ticketpartner.feature_scan_module.feature_login_scan.domain.model.InsertEventDetailsResponse
import com.example.ticketpartner.feature_scan_module.feature_login_scan.domain.model.InsertTicketTypeListResponse
import com.example.ticketpartner.feature_scan_module.feature_login_scan.domain.model.SearchData
import com.example.ticketpartner.feature_scan_module.feature_qr_scan.domain.model.ScanLog
import javax.inject.Inject

class LocalStorageDataSourceImpl @Inject constructor(private val tpScanDao: TpScanDao) :
    LocalStorageDataSource {
    override suspend fun insertQrCodeList(getQrCodeListResponse: DataItems): Long {
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

    override suspend fun insertTicketTypesLocalDB(ticketName: InsertTicketTypeListResponse): Long {
        return tpScanDao.insertTicketTypes(ticketName)
    }

    override suspend fun getTicketTypesListLocalDB(): List<InsertTicketTypeListResponse> {
        return tpScanDao.getTicketTypes()
    }

    override suspend fun insertCheckInLocalDB(checkInData: CheckInData): Long {
        return tpScanDao.insertCheckInList(checkInData)
    }

    override suspend fun getCheckInDataLocalDb(): List<CheckInData> {
        return tpScanDao.getCheckInDataFromLocalDB()
    }

    override suspend fun insertSearchDataLocalDB(searchData: SearchData): Long {
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
}