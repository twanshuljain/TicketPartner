package com.example.ticketpartner.feature_login.domain.model

data class VerifyEmailForgotPassResponse(
    val data: Result?,
    val error: Any?,
    val message: String?,
    val status_code: Int?
)

data class Result(
    val id: Int?,
    val is_verify: Boolean?,
    val reset_token: String?,
    val reset_token_expiration: String?,
    val user_id: Int?
)