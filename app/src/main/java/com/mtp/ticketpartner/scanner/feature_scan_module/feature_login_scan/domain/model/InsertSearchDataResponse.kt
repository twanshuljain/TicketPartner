package com.mtp.ticketpartner.scanner.feature_scan_module.feature_login_scan.domain.model

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.mtp.ticketpartner.scanner.common.GET_SEARCH_LIST
import kotlinx.parcelize.Parcelize

data class InsertSearchDataResponse(
    val data: List<SearchData?>?,
    val error: Any?,
    val message: String?,
    val status_code: Int?
)
@Parcelize
@Entity(tableName = GET_SEARCH_LIST)
data class SearchData(
    @ColumnInfo(name = "email")
    val email: String?,
    @ColumnInfo(name = "eventId")
    val event_id: Int?,
    @ColumnInfo(name = "isCheckedIn")
    var is_checked_in: Boolean?,
    @ColumnInfo(name = "name")
    val name: String?,
    @ColumnInfo(name = "orderNumber")
    val order_number: String?,
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "orderId")
    val order_ticket_id: Int?,
    @ColumnInfo(name = "paymentType")
    val payment_type: String?
): Parcelable