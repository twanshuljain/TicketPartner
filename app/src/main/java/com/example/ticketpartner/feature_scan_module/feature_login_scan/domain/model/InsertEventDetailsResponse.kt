package com.example.ticketpartner.feature_scan_module.feature_login_scan.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.ticketpartner.common.GET_EVENT_DETAILS

@Entity(tableName = GET_EVENT_DETAILS)
data class InsertEventDetailsResponse(
    val city: String?,
    val country: String?,
    val doorCloseTime: String?,
    val doorOpenTime: String?,
    val eventCoverImage: String?,
    val eventEndDate: String?,
    val eventEndTime: String?,
    val eventStartDate: String?,
    val eventStartTime: String?,
    @PrimaryKey
    val id: Int?,
    val name: String?,
    val organizationCountryName: String?,
    val organizationLogo: String?,
    val organizationName: String?,
    val state: String?,
)
