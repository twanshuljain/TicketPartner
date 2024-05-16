package com.example.ticketpartner.feature_local_storage.domain.repository

import com.example.ticketpartner.feature_scan_module.feature_login_scan.domain.model.DataItems

interface LocalStorageRepository {
    suspend fun insertQrCodeList(getQrCodeListResponse: DataItems): Long
    suspend fun getQrCodeListFromLocalDB(): List<DataItems>
}