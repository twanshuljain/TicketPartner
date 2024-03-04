package com.example.ticketpartner.feature_signup.data.datasource

import com.example.ticketpartner.common.remote.apis.RestApiService
import com.example.ticketpartner.feature_signup.domain.datasource.SignUpDataSource
import com.example.ticketpartner.feature_signup.domain.model.CreateUserAccountRequest
import com.example.ticketpartner.feature_signup.domain.model.CreateUserAccountResponse
import com.example.ticketpartner.feature_signup.domain.model.SendEmailOtpSignUpRequest
import com.example.ticketpartner.feature_signup.domain.model.SendEmailOtpSignUpResponse
import com.example.ticketpartner.feature_signup.domain.model.SendEmailOtpVerifyRequest
import com.example.ticketpartner.feature_signup.domain.model.SendEmailOtpVerifyResponse
import com.example.ticketpartner.feature_signup.domain.model.SendPhoneSignUpOtpResponse
import com.example.ticketpartner.feature_signup.domain.model.SendPhoneSignUpOtpVerifyRequest
import com.example.ticketpartner.feature_signup.domain.model.SendPhoneSignUpOtpVerifyResponse
import com.example.ticketpartner.feature_signup.domain.model.SendSignUpPhoneOtpRequest
import javax.inject.Inject

class SignUpDataSourceImpl @Inject constructor(private val restApiService: RestApiService) :
    SignUpDataSource {

    override suspend fun sendEmailOtpSignUp(email: String): SendEmailOtpSignUpResponse {
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
        return restApiService.sendPhoneOtpVerifySignUp(
            SendPhoneSignUpOtpVerifyRequest(
                countryCode,
                phoneNumber,
                otp
            )
        )
    }

    override suspend fun createUserAccount(createUserAccountRequest: CreateUserAccountRequest): CreateUserAccountResponse {
        return restApiService.createUserAccount(createUserAccountRequest)
    }

}