package com.example.ticketpartner.feature_login.domain.datasource

import com.example.ticketpartner.feature_login.domain.model.ForgotPassSendEmailResponse
import com.example.ticketpartner.feature_login.domain.model.ResetPasswordResponse
import com.example.ticketpartner.feature_login.domain.model.SendEmailLinkForgotPasswordResponse
import com.example.ticketpartner.feature_login.domain.model.UserLoginPhoneResponse
import com.example.ticketpartner.feature_login.domain.model.UserLoginResponse
import com.example.ticketpartner.feature_login.domain.model.VerifyEmailForgotPassResponse
import com.example.ticketpartner.feature_login.domain.model.VerifyMobileOtpResponse

/**
 * LoginDataSourceImpl is responsible to set/get data from Data store preference
 * */
interface LoginDataSource {
    suspend fun loginUserEmail(email: String, password: String): UserLoginResponse
    suspend fun sendOtpLogin(countryCode: String, number: String): VerifyMobileOtpResponse
    suspend fun loginUserByPhone(
        countryCode: String,
        number: String,
        otp: String
    ): UserLoginPhoneResponse

    suspend fun forgotPasswordSendEmailLink(email: String): SendEmailLinkForgotPasswordResponse
    suspend fun forgotPasswordSendEmail(email: String): ForgotPassSendEmailResponse
    suspend fun verifyEmailForgotPass(email: String, otp: String): VerifyEmailForgotPassResponse
    suspend fun resetPassword(
        newPassword: String,
        conPassword: String
    ): ResetPasswordResponse

}