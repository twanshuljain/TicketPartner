package com.example.ticketpartner.feature_scan_module.feature_qr_scan.domain.model

data class QrScanReportAllResponse(
    val `data`: DataList?,
    val error: Any?,
    val message: Any?,
    val status_code: Int?
)

data class DataList(
    val online: Int,
    val physical: Int,
    val ticket_data: List<TicketDataList?>?,
    val total_accepted: Int,
    val total_rejected: Int,
    val total_ticket_ratio: TotalTicketRatio,
    val total_tickets: Int
)

data class TicketDataList(
    val ratio: Double?,
    val ticket_name: String?,
    val ticket_type: String?,
    val total_scanned: Int?,
    val total_ticket: Int?
)

data class TotalTicketRatio(
    val accepted_ratio: Double?,
    val online: Double?,
    val physical: Double?,
    val rejected_ratio: Double?,
    val total: Int?
)