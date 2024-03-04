package com.example.ticketpartner.feature_add_organization.domain.model

data class AddOrgSocialRequest(
    val facebook_url: String?,
    val linkedin_url: String?,
    val organization_id: String?,
    val twitter_url: String?,
    val website_url: String?
)