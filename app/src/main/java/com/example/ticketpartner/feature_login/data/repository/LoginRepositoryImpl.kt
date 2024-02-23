package com.example.ticketpartner.feature_login.data.repository

import com.example.ticketpartner.feature_login.domain.datasource.LoginDataSource
import com.example.ticketpartner.feature_login.domain.model.CreateUserAccountRequest
import com.example.ticketpartner.feature_login.domain.model.CreateUserAccountResponse
import com.example.ticketpartner.feature_login.domain.model.ForgotPassSendEmailResponse
import com.example.ticketpartner.feature_login.domain.model.ResetPasswordResponse
import com.example.ticketpartner.feature_login.domain.model.SendEmailOtpResponseSignUp
import com.example.ticketpartner.feature_login.domain.model.SendEmailOtpVerifyResponse
import com.example.ticketpartner.feature_login.domain.model.SendPhoneSignUpOtpResponse
import com.example.ticketpartner.feature_login.domain.model.SendPhoneSignUpOtpVerifyResponse
import com.example.ticketpartner.feature_login.domain.model.UserLoginPhoneResponse
import com.example.ticketpartner.feature_login.domain.model.UserLoginResponse
import com.example.ticketpartner.feature_login.domain.model.VerifyEmailForgotPassResponse
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

    override suspend fun sendEmailOtpSignUp(
        email: String
    ): SendEmailOtpResponseSignUp {
        return loginDataSource.sendEmailOtpSignUp(email)
    }

    override suspend fun sendEmailOtpVerify(
        email: String,
        otpNumber: String
    ): SendEmailOtpVerifyResponse {
        return loginDataSource.sendEmailOtpVerify(email, otpNumber)
    }

    override suspend fun sendPhoneOtpSignUp(
        countryCode: String,
        phoneNumber: String
    ): SendPhoneSignUpOtpResponse {
        return loginDataSource.sendPhoneOtpSignUp(countryCode, phoneNumber)
    }

    override suspend fun sendPhoneOtpVerifySignUp(
        countryCode: String,
        phoneNumber: String,
        otp: String
    ): SendPhoneSignUpOtpVerifyResponse {
        return loginDataSource.sendPhoneOtpVerifySignUp(countryCode, phoneNumber, otp)
    }

    override suspend fun createUserAccount(createUserAccountRequest: CreateUserAccountRequest): CreateUserAccountResponse {
        return loginDataSource.createUserAccount(createUserAccountRequest)
    }

    override suspend fun forgotPasswordSendEmail(email: String): ForgotPassSendEmailResponse {
        return loginDataSource.forgotPasswordSendEmail(email)
    }

    override suspend fun verifyEmailForgotPass(
        email: String,
        otp: String
    ): VerifyEmailForgotPassResponse {
        return loginDataSource.verifyEmailForgotPass(email, otp)
    }

    override suspend fun resetPassword(
        newPassword: String,
        conPassword: String
    ): ResetPasswordResponse {
        return loginDataSource.resetPassword(newPassword, conPassword)
    }

}