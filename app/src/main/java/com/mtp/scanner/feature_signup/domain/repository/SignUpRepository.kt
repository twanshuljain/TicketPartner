package com.mtp.scanner.feature_signup.domain.repository

import com.mtp.scanner.feature_signup.domain.model.CreateUserAccountRequest
import com.mtp.scanner.feature_signup.domain.model.CreateUserAccountResponse
import com.mtp.scanner.feature_signup.domain.model.SendEmailOtpSignUpResponse
import com.mtp.scanner.feature_signup.domain.model.SendEmailOtpVerifyResponse
import com.mtp.scanner.feature_signup.domain.model.SendPhoneSignUpOtpResponse
import com.mtp.scanner.feature_signup.domain.model.SendPhoneSignUpOtpVerifyResponse

interface SignUpRepository {

    suspend fun sendEmailOtpSignUp(email: String): SendEmailOtpSignUpResponse
    suspend fun sendEmailOtpVerify(email: String, otpNumber: String): SendEmailOtpVerifyResponse
    suspend fun sendPhoneOtpSignUp(
        countryCode: String,
        phoneNumber: String
    ): SendPhoneSignUpOtpResponse

    suspend fun sendPhoneOtpVerifySignUp(
        countryCode: String,
        phoneNumber: String,
        otp: String
    ): SendPhoneSignUpOtpVerifyResponse

    suspend fun createUserAccount(createUserAccountRequest: CreateUserAccountRequest): CreateUserAccountResponse
}