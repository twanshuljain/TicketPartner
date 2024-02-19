package com.example.ticketpartner.feature_login.data.repository

import com.example.ticketpartner.feature_login.domain.datasource.LoginDataSource
import com.example.ticketpartner.feature_login.domain.model.UserLoginPhoneResponse
import com.example.ticketpartner.feature_login.domain.model.UserLoginResponse
import com.example.ticketpartner.feature_login.domain.model.VerifyMobileOtpResponse
import com.example.ticketpartner.feature_login.domain.repository.LoginRepository
import javax.inject.Inject

/**
 * Class that requests authentication and user information from the remote data source and
 * maintains an in-memory cache of login status and user credentials information.
 */
class LoginRepositoryImpl @Inject constructor(private val loginDataSource: LoginDataSource) :
    LoginRepository {

    override suspend fun loginUserEmail(email: String, password: String): UserLoginResponse {
        return loginDataSource.loginUserEmail(email, password)
    }

    override suspend fun sendOtpLogin(
        countryCode: String,
        number: String
    ): VerifyMobileOtpResponse {
        return loginDataSource.sendOtpLogin(countryCode, number)
    }

    override suspend fun loginUserByPhone(
        countryCode: String,
        number: String,
        otp: String
    ): UserLoginPhoneResponse {
        return loginDataSource.loginUserByPhone(countryCode, number, otp)
    }

}