package com.example.ticketpartner.feature_scan_module.feature_login_scan.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.ticketpartner.common.GET_TICKET_TYPES_LIST

@Entity(tableName = GET_TICKET_TYPES_LIST)
data class InsertTicketTypeListResponse(
    @PrimaryKey()
    val ticketName: String = "")
