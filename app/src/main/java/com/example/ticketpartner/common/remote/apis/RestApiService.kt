package com.example.ticketpartner.common.remote.apis

import com.example.ticketpartner.feature_login.domain.model.CreateUserAccountRequest
import com.example.ticketpartner.feature_login.domain.model.CreateUserAccountResponse
import com.example.ticketpartner.feature_login.domain.model.ForgotPassSendEmailRequest
import com.example.ticketpartner.feature_login.domain.model.ForgotPassSendEmailResponse
import com.example.ticketpartner.feature_login.domain.model.ResetPasswordRequest
import com.example.ticketpartner.feature_login.domain.model.ResetPasswordResponse
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
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.POST

/**
 *  Rest API
 */
interface RestApiService {

    @POST(loginWithEmail)
    suspend fun loginUserEmail(@Body loginRequest: UserLoginEmailRequest): UserLoginResponse

    @POST(otpVerifyMobile)
    suspend fun sendOtpLogin(@Body verifyMobileOtpRequest: VerifyMobileOtpRequest): VerifyMobileOtpResponse

    @POST(loginWithMobile)
    suspend fun loginUserByPhone(@Body loginPhoneRequest: UserLoginPhoneRequest): UserLoginPhoneResponse

    @POST(sendOtpEmailSignUp)
    suspend fun sendEmailOtpSingUp(@Body sendEmailOtpSignUpRequest: SendEmailOtpSignUpRequest): SendEmailOtpResponseSignUp

    @POST(sendOtpEmailVerifySignUp)
    suspend fun sendEmailOtpVerify(@Body sendEmailOtpVerifyRequest: SendEmailOtpVerifyRequest): SendEmailOtpVerifyResponse

    @POST(sendPhoneOtpSignUp)
    suspend fun sendPhoneOtpSignUp(@Body sendSignUpPhoneOtpRequest: SendSignUpPhoneOtpRequest): SendPhoneSignUpOtpResponse

    @POST(sendPhoneOtpVerifySignUp)
    suspend fun sendPhoneOtpVerifySignUp(@Body sendPhoneSignUpOtpVerifyRequest: SendPhoneSignUpOtpVerifyRequest): SendPhoneSignUpOtpVerifyResponse

    @POST(createUserAccount)
    suspend fun createUserAccount(@Body createUserAccountRequest: CreateUserAccountRequest): CreateUserAccountResponse

    @POST(forgotPassSendEmail)
    suspend fun forgotPassSendEmail(@Body forgotPassSendEmailRequest: ForgotPassSendEmailRequest): ForgotPassSendEmailResponse

    @POST(verifyEmailForgotPass)
    suspend fun verifyEmailForgotPass(@Body verifyEmailForgotPassRequest: VerifyEmailForgotPassRequest): VerifyEmailForgotPassResponse

    //@Headers("Content-Type: application/json")
    @POST(resetPassword)
    suspend fun resetPassword(
        @Body resetPasswordRequest: ResetPasswordRequest
    ): ResetPasswordResponse

}
