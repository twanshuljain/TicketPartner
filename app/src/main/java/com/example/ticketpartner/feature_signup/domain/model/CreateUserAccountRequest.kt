package com.example.ticketpartner.feature_signup.domain.model

data class CreateUserAccountRequest(
    val country_code: String?,
    val email: String?,
    val first_name: String?,
    val last_name: String?,
    val mobile_number: String?,
    val password: String?
)