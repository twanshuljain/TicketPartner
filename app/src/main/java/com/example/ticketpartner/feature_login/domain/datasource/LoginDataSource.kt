package com.example.ticketpartner.feature_login.domain.datasource

import com.example.ticketpartner.feature_login.domain.model.UserLoginPhoneResponse
import com.example.ticketpartner.feature_login.domain.model.UserLoginResponse
import com.example.ticketpartner.feature_login.domain.model.VerifyMobileOtpResponse

/**
 * LoginDataSourceImpl is responsible to set/get data from Data store preference
 * */
interface LoginDataSource {
    suspend fun loginUserEmail(email: String, password: String): UserLoginResponse
    suspend fun sendOtpLogin(countryCode: String, number: String): VerifyMobileOtpResponse
    suspend fun loginUserByPhone(countryCode: String, number: String,otp: String): UserLoginPhoneResponse

}