package com.example.ticketpartner.feature_login.data.datasource

import com.example.ticketpartner.common.remote.apis.RestApiService
import com.example.ticketpartner.feature_login.domain.datasource.LoginDataSource
import com.example.ticketpartner.feature_login.domain.model.CreateUserAccountRequest
import com.example.ticketpartner.feature_login.domain.model.CreateUserAccountResponse
import com.example.ticketpartner.feature_login.domain.model.ForgotPassSendEmailRequest
import com.example.ticketpartner.feature_login.domain.model.ForgotPassSendEmailResponse
import com.example.ticketpartner.feature_login.domain.model.SendEmailOtpResponseSignUp
import com.example.ticketpartner.feature_login.domain.model.SendEmailOtpSignUpRequest
import com.example.ticketpartner.feature_login.domain.model.SendEmailOtpVerifyRequest
import com.example.ticketpartner.feature_login.domain.model.SendEmailOtpVerifyResponse
import com.example.ticketpartner.feature_login.domain.model.SendPhoneSignUpOtpResponse
import com.example.ticketpartner.feature_login.domain.model.SendPhoneSignUpOtpVerifyRequest
import com.example.ticketpartner.feature_login.domain.model.SendPhoneSignUpOtpVerifyResponse
import com.example.ticketpartner.feature_login.domain.model.SendSignUpPhoneOtpRequest
import com.example.ticketpartner.feature_login.domain.model.UserLoginEmailRequest
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

    override suspend fun sendEmailOtpSignUp(email: String): SendEmailOtpResponseSignUp {
        return restApiService.sendEmailOtpSingUp(
            SendEmailOtpSignUpRequest(
                email = email
            )
        )
    }

    override suspend fun sendEmailOtpVerify(
        email: String,
        otpNumber: String
    ): SendEmailOtpVerifyResponse {
        return restApiService.sendEmailOtpVerify(
            SendEmailOtpVerifyRequest(
                email = email,
                otp = otpNumber
            )
        )
    }

    override suspend fun sendPhoneOtpSignUp(
        countryCode: String,
        phoneNumber: String
    ): SendPhoneSignUpOtpResponse {
        return restApiService.sendPhoneOtpSignUp(
            SendSignUpPhoneOtpRequest(
                countryCode,
                phoneNumber
            )
        )
    }

    override suspend fun sendPhoneOtpVerifySignUp(
        countryCode: String,
        phoneNumber: String,
        otp: String
    ): SendPhoneSignUpOtpVerifyResponse {
        return restApiService.sendPhoneOtpVerifySignUp(SendPhoneSignUpOtpVerifyRequest(countryCode,phoneNumber,otp))
    }

    override suspend fun createUserAccount(createUserAccountRequest: CreateUserAccountRequest): CreateUserAccountResponse {
        return restApiService.createUserAccount(createUserAccountRequest)
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

}