package com.example.ticketpartner.feature_add_organization.domain.model

data class SearchCountryResponse(
    val data: List<SearchItem?>?,
    val error: Any?,
    val message: String?,
    val status_code: Int?
)

data class SearchItem(
    val country_code: String?,
    val country_name: String?,
    val created_at: String?,
    val created_by: Any?,
    val id: Int?,
    val image: Any?,
    val is_active: Boolean?,
    val is_caribbean: String?,
    val sort: String?,
    val updated_at: String?,
    val updated_by: Any?
)