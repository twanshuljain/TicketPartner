package com.example.ticketpartner.feature_add_organization.domain.model

data class AddOrganizationResponse(
    val data: Data?,
    val error: Any?,
    val message: String?,
    val status_code: Int?
)

data class Data(
    val country_id: Int?,
    val created_at: String?,
    val created_by: Any?,
    val description: Any?,
    val facebook_url: Any?,
    val id: Int?,
    val is_active: Boolean?,
    val linkedin_url: Any?,
    val name: String?,
    val organization_logo: String?,
    val twitter_url: Any?,
    val updated_at: String?,
    val updated_by: Any?,
    val user_id: Int?,
    val website_url: Any?
)