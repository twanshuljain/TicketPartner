package com.example.ticketpartner.feature_local_storage.data.repository

import com.example.ticketpartner.feature_local_storage.domain.datasourse.LocalStorageDataSource
import com.example.ticketpartner.feature_local_storage.domain.repository.LocalStorageRepository
import com.example.ticketpartner.feature_scan_module.feature_login_scan.domain.model.DataItems
import com.example.ticketpartner.feature_scan_module.feature_login_scan.domain.model.InsertEventDetailsResponse
import javax.inject.Inject

class LocalStorageRepositoryImpl @Inject constructor(private val localStorageDataSource: LocalStorageDataSource):
    LocalStorageRepository {
    override suspend fun insertQrCodeList(getQrCodeListResponse: DataItems): Long {
        return localStorageDataSource.insertQrCodeList(getQrCodeListResponse)
    }

    override suspend fun getQrCodeListFromLocalDB(): List<DataItems> {
        return localStorageDataSource.getQrCodeListFromLocalDB()
    }

    override suspend fun insertEventDetailsLocalDB(insertEventDetailsResponse: InsertEventDetailsResponse): Long {
        return localStorageDataSource.insertEventDetailsLocalDB(insertEventDetailsResponse)

    }
}