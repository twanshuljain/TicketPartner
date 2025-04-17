package com.mtp.ticketpartner.scanner.feature_scan_module.feature_qr_scan.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

data class SearchApiScanResponse(
    val `data`: List<MData?>?,
    val error: Any?,
    val message: String?,
    val status_code: Int?
)

@Parcelize
data class MData(
    var email: String?,
    var name: String?,
    var order_id: String = "",
    var payment_type: String?,
    var is_checked_in: Boolean? = false,
    var ticket_name: String = "",
    var is_checkin_allowed: Boolean,
    var is_refunded: Boolean,
    var is_partial_payment: Boolean,
    var all_emi_collected: Boolean,
) : Parcelable