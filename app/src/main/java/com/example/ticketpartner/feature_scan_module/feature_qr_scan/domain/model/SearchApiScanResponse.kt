package com.example.ticketpartner.feature_scan_module.feature_qr_scan.domain.model

data class SearchApiScanResponse(
    val `data`: List<DataList?>?,
    val error: Any?,
    val message: String?,
    val status_code: Int?
)

data class DataList(
    val id: String?,
    val order_id: String?,
    val payment_type: String?
)