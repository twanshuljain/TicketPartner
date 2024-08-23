package com.mtp.scanner.feature_scan_module.feature_qr_scan.domain.model

data class ScanReportTicketNameResponse(
    val data: DataItem?,
    val error: Any?,
    val message: String?,
    val status_code: Int?
)

data class DataItem(
    val ticket_data: List<TicketData?>?,
    val total_accepted: Int?,
    val total_rejected: Int?,
    val total_tickets: Int?
)

data class TicketData(
    val ratio: Double?,
    val ticket_name: String?,
    val ticket_type: String?,
    val total_scanned: Int?,
    val total_ticket: Int?
)