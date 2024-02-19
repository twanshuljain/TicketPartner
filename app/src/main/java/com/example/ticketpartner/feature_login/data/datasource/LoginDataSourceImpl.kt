package com.example.ticketpartner.feature_login.data.datasource

import com.example.ticketpartner.common.remote.apis.RestApiService
import com.example.ticketpartner.feature_login.domain.datasource.LoginDataSource
import com.example.ticketpartner.feature_login.domain.model.UserLoginEmailRequest
import com.example.ticketpartner.feature_login.domain.model.UserLoginPhoneRequest
import com.example.ticketpartner.feature_login.domain.model.UserLoginPhoneResponse
import com.example.ticketpartner.feature_login.domain.model.UserLoginResponse
import com.example.ticketpartner.feature_login.domain.model.VerifyMobileOtpRequest
import com.example.ticketpartner.feature_login.domain.model.VerifyMobileOtpResponse
import javax.inject.Inject

/**
 * LoginDataSourceImpl is responsible to set/get data from Data store preference
 * */
class LoginDataSourceImpl @Inject constructor(private val restApiService: RestApiService) :
    LoginDataSource {

    override suspend fun loginUserEmail(email: String, password: String): UserLoginResponse {
        return restApiService.loginUserEmail(UserLoginEmailRequest(email, password))
    }

    override suspend fun sendOtpLogin(
        countryCode: String,
        number: String
    ): VerifyMobileOtpResponse {
        return restApiService.sendOtpLogin(VerifyMobileOtpRequest(countryCode, number))
    }

    override suspend fun loginUserByPhone(
        countryCode: String,
        number: String,
        otp: String
    ): UserLoginPhoneResponse {
        return restApiService.loginUserByPhone(
            UserLoginPhoneRequest(
                country_code = countryCode,
                mobile_number = number,
                otp = otp
            )
        )
    }

}