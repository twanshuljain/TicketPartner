package com.example.ticketpartner.feature_add_organization.domain.model

data class AddOrgSocialResponse(
    val `data`: DataItem?,
    val error: Any?,
    val message: String?,
    val status_code: Int?
)

data class DataItem(
    val country_id: Int?,
    val created_at: String?,
    val created_by: Any?,
    val description: Any?,
    val facebook_url: String?,
    val id: Int?,
    val is_active: Boolean?,
    val linkedin_url: String?,
    val name: String?,
    val organization_logo: String?,
    val twitter_url: String?,
    val updated_at: String?,
    val updated_by: Any?,
    val user_id: Int?,
    val website_url: String?
)