package com.mtp.scanner.feature_login.domain.repository

import com.mtp.scanner.feature_login.domain.model.ForgotPassSendEmailResponse
import com.mtp.scanner.feature_login.domain.model.ResetPasswordResponse
import com.mtp.scanner.feature_login.domain.model.SendEmailLinkForgotPasswordResponse
import com.mtp.scanner.feature_login.domain.model.UserLoginPhoneResponse
import com.mtp.scanner.feature_login.domain.model.UserLoginResponse
import com.mtp.scanner.feature_login.domain.model.VerifyEmailForgotPassResponse
import com.mtp.scanner.feature_login.domain.model.VerifyMobileOtpResponse

interface LoginRepository {

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