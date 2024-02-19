package com.example.ticketpartner.feature_login.domain.repository

import com.example.ticketpartner.feature_login.domain.model.UserLoginPhoneResponse
import com.example.ticketpartner.feature_login.domain.model.UserLoginResponse
import com.example.ticketpartner.feature_login.domain.model.VerifyMobileOtpResponse

interface LoginRepository {

    suspend fun loginUserEmail(email: String, password: String): UserLoginResponse
    suspend fun sendOtpLogin(countryCode: String, number: String): VerifyMobileOtpResponse
    suspend fun loginUserByPhone(countryCode: String, number: String,otp: String): UserLoginPhoneResponse

}