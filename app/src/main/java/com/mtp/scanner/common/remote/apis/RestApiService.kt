package com.mtp.scanner.common.remote.apis

import com.mtp.scanner.feature_add_organization.domain.model.AddOrgSocialRequest
import com.mtp.scanner.feature_add_organization.domain.model.AddOrgSocialResponse
import com.mtp.scanner.feature_add_organization.domain.model.AddOrganizationResponse
import com.mtp.scanner.feature_add_organization.domain.model.SearchCountryResponse
import com.mtp.scanner.feature_create_event.domain.model.CreateEventGetTimeZoneResponse
import com.mtp.scanner.feature_create_event.domain.model.CreateEventTicketListResponse
import com.mtp.scanner.feature_create_event.domain.model.CreateEventTypesResponse
import com.mtp.scanner.feature_create_event.domain.model.CreateEventVenueStateResponse
import com.mtp.scanner.feature_login.domain.model.ForgotPassSendEmailRequest
import com.mtp.scanner.feature_login.domain.model.ForgotPassSendEmailResponse
import com.mtp.scanner.feature_login.domain.model.ResetPasswordRequest
import com.mtp.scanner.feature_login.domain.model.ResetPasswordResponse
import com.mtp.scanner.feature_login.domain.model.SendEmailLinkForgotPasswordRequest
import com.mtp.scanner.feature_login.domain.model.SendEmailLinkForgotPasswordResponse
import com.mtp.scanner.feature_login.domain.model.UserEmailLoginRequest
import com.mtp.scanner.feature_login.domain.model.UserLoginPhoneRequest
import com.mtp.scanner.feature_login.domain.model.UserLoginPhoneResponse
import com.mtp.scanner.feature_login.domain.model.UserLoginResponse
import com.mtp.scanner.feature_login.domain.model.VerifyEmailForgotPassRequest
import com.mtp.scanner.feature_login.domain.model.VerifyEmailForgotPassResponse
import com.mtp.scanner.feature_login.domain.model.VerifyMobileOtpRequest
import com.mtp.scanner.feature_login.domain.model.VerifyMobileOtpResponse
import com.mtp.scanner.feature_scan_module.feature_login_scan.domain.model.EventDetailsScanResponse
import com.mtp.scanner.feature_scan_module.feature_login_scan.domain.model.GetCheckInDataOfflineResponse
import com.mtp.scanner.feature_scan_module.feature_login_scan.domain.model.GetQrCodeListResponse
import com.mtp.scanner.feature_scan_module.feature_login_scan.domain.model.InsertSearchDataResponse
import com.mtp.scanner.feature_scan_module.feature_login_scan.domain.model.LoginWithPinRequest
import com.mtp.scanner.feature_scan_module.feature_login_scan.domain.model.LoginWithPinResponse
import com.mtp.scanner.feature_scan_module.feature_qr_scan.domain.model.QrScanReportAllResponse
import com.mtp.scanner.feature_scan_module.feature_qr_scan.domain.model.QrScanRequest
import com.mtp.scanner.feature_scan_module.feature_qr_scan.domain.model.QrScanResponse
import com.mtp.scanner.feature_scan_module.feature_qr_scan.domain.model.QrScannedTicketResponse
import com.mtp.scanner.feature_scan_module.feature_qr_scan.domain.model.ScanCheckedInRequest
import com.mtp.scanner.feature_scan_module.feature_qr_scan.domain.model.ScanCheckedInResponse
import com.mtp.scanner.feature_scan_module.feature_qr_scan.domain.model.ScanSearchOrderDetailsResponse
import com.mtp.scanner.feature_scan_module.feature_qr_scan.domain.model.SearchApiScanResponse
import com.mtp.scanner.feature_scan_module.feature_qr_scan.domain.model.SendScanLogOfflineRequest
import com.mtp.scanner.feature_scan_module.feature_qr_scan.domain.model.UploadScanLogDataServerResponse
import com.mtp.scanner.feature_signup.domain.model.CreateUserAccountRequest
import com.mtp.scanner.feature_signup.domain.model.CreateUserAccountResponse
import com.mtp.scanner.feature_signup.domain.model.SendEmailOtpSignUpRequest
import com.mtp.scanner.feature_signup.domain.model.SendEmailOtpSignUpResponse
import com.mtp.scanner.feature_signup.domain.model.SendEmailOtpVerifyRequest
import com.mtp.scanner.feature_signup.domain.model.SendEmailOtpVerifyResponse
import com.mtp.scanner.feature_signup.domain.model.SendPhoneSignUpOtpResponse
import com.mtp.scanner.feature_signup.domain.model.SendPhoneSignUpOtpVerifyRequest
import com.mtp.scanner.feature_signup.domain.model.SendPhoneSignUpOtpVerifyResponse
import com.mtp.scanner.feature_signup.domain.model.SendSignUpPhoneOtpRequest
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.PartMap
import retrofit2.http.Path
import retrofit2.http.Query

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

    @GET(getStateBasedOnCountryId)
    suspend fun getStateBasedOnCountry(@Path("countryId") countryId: Int): CreateEventVenueStateResponse

    @GET(getCreateEventTicketList)
    suspend fun getCreateEventTicketList(@Path("eventId") eventId: Int): CreateEventTicketListResponse

    //scan module
    @POST(loginWithPin)
    suspend fun loginWithPin(@Body loginWithPinRequest: LoginWithPinRequest): LoginWithPinResponse

    @GET(getScanEventDetails)
    suspend fun getScanEventDetails(): EventDetailsScanResponse

    @POST(scanQrCode)
    suspend fun scanQrCode(@Body qrScanRequest: QrScanRequest): QrScanResponse

    @GET(getQrScannedTicketData)
    suspend fun getQrScannedTicketData(): QrScannedTicketResponse

    @GET(getQrScannedSearchData)
    suspend fun getQrScannedSearchData(@Query("search") orderId: String): SearchApiScanResponse

    @GET(getQrScannedSearchDataOffline)
    suspend fun getQrScannedSearchAllDataOffline(): InsertSearchDataResponse

    @GET(getQrOrderDetailsData)
    suspend fun getQrScanOrderDetailsData(@Query("order_id") orderId: String): ScanSearchOrderDetailsResponse

    @POST(getQrCheckedIn)
    suspend fun getQrScanCheckedInData(@Body scanCheckedInRequest: ScanCheckedInRequest): ScanCheckedInResponse

    @GET(getQrScanReportAll)
    suspend fun getQrScanReportAllData(@Query("report_type") type: String): QrScanReportAllResponse

    @GET(getQrCodeListForOfflineScan)
    suspend fun getQrListForOfflineScan(): GetQrCodeListResponse

    @GET(getCheckInList)
    suspend fun getCheckInListForOffline(): GetCheckInDataOfflineResponse

    @POST(getUploadScanLogData)
    suspend fun uploadScanLogDataServer(@Body sendScanLogOfflineRequest: SendScanLogOfflineRequest): UploadScanLogDataServerResponse

}
