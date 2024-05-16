package com.example.ticketpartner.feature_local_storage.data.datasource

import com.example.ticketpartner.common.localDatabase.TpScanDao
import com.example.ticketpartner.feature_local_storage.domain.datasourse.LocalStorageDataSource
import com.example.ticketpartner.feature_scan_module.feature_login_scan.domain.model.DataItems
import javax.inject.Inject

class LocalStorageDataSourceImpl @Inject constructor(private val tpScanDao: TpScanDao): LocalStorageDataSource{
    override suspend fun insertQrCodeList(getQrCodeListResponse: DataItems): Long {
        return tpScanDao.insertOfflineScanData(getQrCodeListResponse)
    }

    override suspend fun getQrCodeListFromLocalDB(): List<DataItems> {
        return tpScanDao.getQrCodeListFromLocalDB()
    }
}