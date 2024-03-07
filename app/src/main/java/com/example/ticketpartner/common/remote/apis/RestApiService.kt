package com.example.ticketpartner.common.remote.apis

import com.example.ticketpartner.feature_add_organization.domain.model.AddOrgSocialRequest
import com.example.ticketpartner.feature_add_organization.domain.model.AddOrgSocialResponse
import com.example.ticketpartner.feature_add_organization.domain.model.AddOrganizationResponse
import com.example.ticketpartner.feature_add_organization.domain.model.SearchCountryResponse
import com.example.ticketpartner.feature_create_event.domain.model.CreateEventGetTimeZoneResponse
import com.example.ticketpartner.feature_create_event.domain.model.CreateEventTypesResponse
import com.example.ticketpartner.feature_signup.domain.model.CreateUserAccountRequest
import com.example.ticketpartner.feature_signup.domain.model.CreateUserAccountResponse
import com.example.ticketpartner.feature_login.domain.model.ForgotPassSendEmailRequest
import com.example.ticketpartner.feature_login.domain.model.ForgotPassSendEmailResponse
import com.example.ticketpartner.feature_login.domain.model.ResetPasswordRequest
import com.example.ticketpartner.feature_login.domain.model.ResetPasswordResponse
import com.example.ticketpartner.feature_login.domain.model.SendEmailLinkForgotPasswordRequest
import com.example.ticketpartner.feature_login.domain.model.SendEmailLinkForgotPasswordResponse
import com.example.ticketpartner.feature_signup.domain.model.SendEmailOtpSignUpResponse
import com.example.ticketpartner.feature_signup.domain.model.SendEmailOtpSignUpRequest
import com.example.ticketpartner.feature_signup.domain.model.SendEmailOtpVerifyRequest
import com.example.ticketpartner.feature_signup.domain.model.SendEmailOtpVerifyResponse
import com.example.ticketpartner.feature_signup.domain.model.SendPhoneSignUpOtpResponse
import com.example.ticketpartner.feature_signup.domain.model.SendPhoneSignUpOtpVerifyRequest
import com.example.ticketpartner.feature_signup.domain.model.SendPhoneSignUpOtpVerifyResponse
import com.example.ticketpartner.feature_signup.domain.model.SendSignUpPhoneOtpRequest
import com.example.ticketpartner.feature_login.domain.model.UserEmailLoginRequest
import com.example.ticketpartner.feature_login.domain.model.UserLoginPhoneRequest
import com.example.ticketpartner.feature_login.domain.model.UserLoginPhoneResponse
import com.example.ticketpartner.feature_login.domain.model.UserLoginResponse
import com.example.ticketpartner.feature_login.domain.model.VerifyEmailForgotPassRequest
import com.example.ticketpartner.feature_login.domain.model.VerifyEmailForgotPassResponse
import com.example.ticketpartner.feature_login.domain.model.VerifyMobileOtpRequest
import com.example.ticketpartner.feature_login.domain.model.VerifyMobileOtpResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.PartMap

/**
 *  Rest API
 */
interface RestApiService {

    @POST(loginWithEmail)
    suspend fun loginUserEmail(@Body loginRequest: UserEmailLoginRequest): UserLoginResponse

    @POST(otpVerifyMobile)
    suspend fun sendOtpLogin(@Body verifyMobileOtpRequest: VerifyMobileOtpRequest): VerifyMobileOtpResponse

    @POST(loginWithMobile)
    suspend fun loginUserByPhone(@Body loginPhoneRequest: UserLoginPhoneRequest): UserLoginPhoneResponse

    @POST(sendEmailLinkForgotPassword)
    suspend fun sendEmailLinkForgotPassword(@Body sendEmailLinkForgotPasswordRequest: SendEmailLinkForgotPasswordRequest): SendEmailLinkForgotPasswordResponse

    @POST(sendOtpEmailSignUp)
    suspend fun sendEmailOtpSingUp(@Body sendEmailOtpSignUpRequest: SendEmailOtpSignUpRequest): SendEmailOtpSignUpResponse

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

    @POST(resetPassword)
    suspend fun resetPassword(
        @Body resetPasswordRequest: ResetPasswordRequest
    ): ResetPasswordResponse

    @Multipart
    @POST(addOrganization)
    suspend fun addOrganization(
        @Part file: MultipartBody.Part?,
        @PartMap textData: Map<String, @JvmSuppressWildcards RequestBody>
    ): AddOrganizationResponse

    @POST(addOrganizationSocial)
    suspend fun addOrganizationSocial(@Body addOrgSocialRequest: AddOrgSocialRequest): AddOrgSocialResponse

    @GET(searchCountry)
    suspend fun searchCountry(): SearchCountryResponse

    @GET(getTimeZone)
    suspend fun getTimeZone(): CreateEventGetTimeZoneResponse

    @GET(getEventType)
    suspend fun getEventType(): CreateEventTypesResponse

}
