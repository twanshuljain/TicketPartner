package com.example.ticketpartner.feature_signup.domain.model

data class CreateUserAccountResponse(
    val data: NewUserDate?,
    val error: Any?,
    val message: String?,
    val status_code: Int?
)

data class NewUserDate(
    val access_token: String?,
    val country_code: String?,
    val email: String?,
    val first_name: String?,
    val id: Int?,
    val last_name: String?,
    val mobile_number: String?,
    val refresh_token: String?,
    val user_status: Any?
)