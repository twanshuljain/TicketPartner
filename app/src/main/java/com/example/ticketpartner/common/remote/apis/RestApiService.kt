package com.example.ticketpartner.common.remote.apis

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
import retrofit2.http.Body
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


    /* @GET("/us/{pin}")
     suspend fun getAddressData(@Path("pin") pinCode: String): AddressData


     @GET(GET_BRANCH_BY_ID)
     suspend fun getBranchById(
         @Query(CURRENT_PAGE) currentPage: Int,
         @Query(FIELDS) fields: String?,
         @Query(JOB_ID) jobID: String?,
         @Query(PAGE_SIZE) pageSize: Int,
         @Query(SORT) sort: String? = null,
     ): BranchListResponse


     @POST(POST_WISH_LIST)
     suspend fun addToList(
         @Query(PRODUCT_CODE) code: String?,
         @Query(PRODUCT_QUANTITY) quantity: String?,
         @Query(UOM) uom: String?,
         @Query(LIST_CART_CODES, encoded = true) listCartCode: String
     ): ResponseBody


     @FormUrlEncoded
     @POST(ADD_TO_CART_MY_LIST)
     suspend fun addToCartMyList(
         @Path(value = "cardId", encoded = true) listCode: String?,
         @Field("preventSaveActiveCart") isPreventSaveActiveCart: Boolean?,
         @Field("keepRestoredCart") isKeepRestoredCart: Boolean?,
         @Field("cartName") cartName: String?,
     ): Response<Unit>

     @Multipart
     @POST(CREATE_JOB)
     suspend fun createJob(
         @Part file: MultipartBody.Part?,
         @Part("jobData") createJobRequest: CreateJobRequest?
     ): Response<Unit>
     */
}