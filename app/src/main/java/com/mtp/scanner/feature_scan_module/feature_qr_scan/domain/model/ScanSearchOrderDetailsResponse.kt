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
    val order_number: String?,
    val stripe_obj: StripeObj ?= null
)

data class StripeObj(
    var payment_method_types: String?,
    val stripe_customer_id: Int?,
    val stripe_customer: String?,
    val is_active: Boolean?,
    val converted_currency_amount_without_service_fee: String?,
    val customer_email: String?,
    val is_ticket_download: Boolean?,
)