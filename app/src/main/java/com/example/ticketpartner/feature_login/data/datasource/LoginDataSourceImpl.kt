package com.example.ticketpartner.feature_login.data.datasource

import com.example.ticketpartner.common.remote.apis.RestApiService
import com.example.ticketpartner.feature_login.domain.datasource.LoginDataSource
import com.example.ticketpartner.feature_login.domain.model.ForgotPassSendEmailRequest
import com.example.ticketpartner.feature_login.domain.model.ForgotPassSendEmailResponse
import com.example.ticketpartner.feature_login.domain.model.ResetPasswordRequest
import com.example.ticketpartner.feature_login.domain.model.ResetPasswordResponse
import com.example.ticketpartner.feature_login.domain.model.SendEmailLinkForgotPasswordRequest
import com.example.ticketpartner.feature_login.domain.model.SendEmailLinkForgotPasswordResponse
import com.example.ticketpartner.feature_login.domain.model.UserEmailLoginRequest
import com.example.ticketpartner.feature_login.domain.model.UserLoginPhoneRequest
import com.example.ticketpartner.feature_login.domain.model.UserLoginPhoneResponse
import com.example.ticketpartner.feature_login.domain.model.UserLoginResponse
import com.example.ticketpartner.feature_login.domain.model.VerifyEmailForgotPassRequest
import com.example.ticketpartner.feature_login.domain.model.VerifyEmailForgotPassResponse
import com.example.ticketpartner.feature_login.domain.model.VerifyMobileOtpRequest
import com.example.ticketpartner.feature_login.domain.model.VerifyMobileOtpResponse
import javax.inject.Inject

/**
 * LoginDataSourceImpl is responsible to set/get data from Data store preference
 * */
class LoginDataSourceImpl @Inject constructor(private val restApiService: RestApiService) :
    LoginDataSource {

    override suspend fun loginUserEmail(email: String, password: String): UserLoginResponse {
        return restApiService.loginUserEmail(UserEmailLoginRequest(email, password))
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

    override suspend fun forgotPasswordSendEmailLink(email: String): SendEmailLinkForgotPasswordResponse {
        return restApiService.sendEmailLinkForgotPassword(SendEmailLinkForgotPasswordRequest(email))
    }

    override suspend fun forgotPasswordSendEmail(email: String): ForgotPassSendEmailResponse {
        return restApiService.forgotPassSendEmail(ForgotPassSendEmailRequest(email))
    }

    override suspend fun verifyEmailForgotPass(
        email: String,
        otp: String
    ): VerifyEmailForgotPassResponse {
        return restApiService.verifyEmailForgotPass(VerifyEmailForgotPassRequest(email, otp))
    }

    override suspend fun resetPassword(
        newPassword: String,
        conPassword: String
    ): ResetPasswordResponse {
        return restApiService.resetPassword(
            ResetPasswordRequest(newPassword, conPassword)
        )
    }


}