package com.mtp.scanner.feature_scan_module.feature_qr_scan.domain.model

data class ScanSearchOrderDetailsResponse(
    val data: List<Item?>?,
    val error: Any?,
    val message: String?,
    val status_code: Int?
)

data class Item(
    var is_checked_in: Boolean?,
    val order_id: Int?,
    val order_number: String?
)