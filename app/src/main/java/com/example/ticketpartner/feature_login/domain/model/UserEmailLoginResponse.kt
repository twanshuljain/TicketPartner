package com.example.ticketpartner.feature_login.domain.model

data class UserLoginResponse(
    val `data`: Data?,
    val error: Any?,
    val message: String?,
    val status_code: Int?
)

data class Data(
    val access_token: String?,
    val email: String?,
    val first_name: String?,
    val id: Int?,
    val last_name: String?,
    val refresh_token: String?,
    val user_status: Any?
)