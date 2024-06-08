package com.example.ticketpartner.feature_local_storage.domain.datasourse

import com.example.ticketpartner.feature_scan_module.feature_login_scan.domain.model.CheckInData
import com.example.ticketpartner.feature_scan_module.feature_login_scan.domain.model.DataItems
import com.example.ticketpartner.feature_scan_module.feature_login_scan.domain.model.InsertEventDetailsResponse
import com.example.ticketpartner.feature_scan_module.feature_login_scan.domain.model.InsertTicketTypeListResponse

interface LocalStorageDataSource {
    suspend fun insertQrCodeList(getQrCodeListResponse: DataItems): Long
    suspend fun getQrCodeListFromLocalDB(): List<DataItems>
    suspend fun insertEventDetailsLocalDB(insertEventDetailsResponse: InsertEventDetailsResponse): Long
    suspend fun getEventDetailsLocalDB(): InsertEventDetailsResponse
    suspend fun insertTicketTypesLocalDB(ticketName: InsertTicketTypeListResponse): Long
    suspend fun getTicketTypesListLocalDB(): List<InsertTicketTypeListResponse>
    suspend fun insertCheckInLocalDB(checkInData: CheckInData): Long
    suspend fun getCheckInDataLocalDb(): List<CheckInData>

}