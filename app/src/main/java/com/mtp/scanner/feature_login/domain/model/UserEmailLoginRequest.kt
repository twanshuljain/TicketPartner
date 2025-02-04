package com.mtp.scanner.feature_login.domain.model

data class UserEmailLoginRequest(
    val email: String?,
    val password: String?
)