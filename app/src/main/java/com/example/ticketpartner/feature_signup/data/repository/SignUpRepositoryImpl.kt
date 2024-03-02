package com.example.ticketpartner.feature_signup.data.repository

import com.example.ticketpartner.feature_signup.domain.datasource.SignUpDataSource
import com.example.ticketpartner.feature_signup.domain.model.CreateUserAccountRequest
import com.example.ticketpartner.feature_signup.domain.model.CreateUserAccountResponse
import com.example.ticketpartner.feature_signup.domain.model.SendEmailOtpSignUpResponse
import com.example.ticketpartner.feature_signup.domain.model.SendEmailOtpVerifyResponse
import com.example.ticketpartner.feature_signup.domain.model.SendPhoneSignUpOtpResponse
import com.example.ticketpartner.feature_signup.domain.model.SendPhoneSignUpOtpVerifyResponse
import com.example.ticketpartner.feature_signup.domain.repository.SignUpRepository
import javax.inject.Inject

class SignUpRepositoryImpl @Inject constructor(private val signUpDataSource: SignUpDataSource) :
    SignUpRepository {
    override suspend fun sendEmailOtpSignUp(
        email: String
    ): SendEmailOtpSignUpResponse {
        return signUpDataSource.sendEmailOtpSignUp(email)
    }

    override suspend fun sendEmailOtpVerify(
        email: String,
        otpNumber: String
    ): SendEmailOtpVerifyResponse {
        return signUpDataSource.sendEmailOtpVerify(email, otpNumber)
    }

    override suspend fun sendPhoneOtpSignUp(
        countryCode: String,
        phoneNumber: String
    ): SendPhoneSignUpOtpResponse {
        return signUpDataSource.sendPhoneOtpSignUp(countryCode, phoneNumber)
    }

    override suspend fun sendPhoneOtpVerifySignUp(
        countryCode: String,
        phoneNumber: String,
        otp: String
    ): SendPhoneSignUpOtpVerifyResponse {
        return signUpDataSource.sendPhoneOtpVerifySignUp(countryCode, phoneNumber, otp)
    }

    override suspend fun createUserAccount(createUserAccountRequest: CreateUserAccountRequest): CreateUserAccountResponse {
        return signUpDataSource.createUserAccount(createUserAccountRequest)
    }
}