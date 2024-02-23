package com.example.ticketpartner.feature_login.domain.repository

import com.example.ticketpartner.feature_login.domain.model.CreateUserAccountRequest
import com.example.ticketpartner.feature_login.domain.model.CreateUserAccountResponse
import com.example.ticketpartner.feature_login.domain.model.ForgotPassSendEmailResponse
import com.example.ticketpartner.feature_login.domain.model.SendEmailOtpResponseSignUp
import com.example.ticketpartner.feature_login.domain.model.SendEmailOtpVerifyResponse
import com.example.ticketpartner.feature_login.domain.model.SendPhoneSignUpOtpResponse
import com.example.ticketpartner.feature_login.domain.model.SendPhoneSignUpOtpVerifyResponse
import com.example.ticketpartner.feature_login.domain.model.UserLoginPhoneResponse
import com.example.ticketpartner.feature_login.domain.model.UserLoginResponse
import com.example.ticketpartner.feature_login.domain.model.VerifyEmailForgotPassResponse
import com.example.ticketpartner.feature_login.domain.model.VerifyMobileOtpResponse

interface LoginRepository {

    suspend fun loginUserEmail(email: String, password: String): UserLoginResponse
    suspend fun sendOtpLogin(countryCode: String, number: String): VerifyMobileOtpResponse
    suspend fun loginUserByPhone(countryCode: String, number: String,otp: String): UserLoginPhoneResponse
    suspend fun sendEmailOtpSignUp(email: String): SendEmailOtpResponseSignUp
    suspend fun sendEmailOtpVerify(email: String,otpNumber: String): SendEmailOtpVerifyResponse
    suspend fun sendPhoneOtpSignUp(countryCode: String,phoneNumber: String): SendPhoneSignUpOtpResponse
    suspend fun sendPhoneOtpVerifySignUp(countryCode: String,phoneNumber: String,otp: String): SendPhoneSignUpOtpVerifyResponse
    suspend fun createUserAccount(createUserAccountRequest: CreateUserAccountRequest): CreateUserAccountResponse
    suspend fun forgotPasswordSendEmail(email: String): ForgotPassSendEmailResponse
    suspend fun verifyEmailForgotPass(email: String,otp: String): VerifyEmailForgotPassResponse

}