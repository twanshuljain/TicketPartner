package com.example.ticketpartner.common.remote.apis

/**
 * Rest end point for anonymous token
 */
const val loginWithEmail = "auth/user/login/"
const val otpVerifyMobile = "auth/user/mobile-login/send-otp/"
const val loginWithMobile = "auth/user/mobile-login/"
const val sendOtpEmailSignUp = "auth/user/sign-up/send/email/otp/"
const val sendOtpEmailVerifySignUp = "auth/user/sign-up/verify/email/otp/"
const val sendPhoneOtpSignUp = "auth/user/sign-up/send/mobile/otp/"
const val sendPhoneOtpVerifySignUp = "auth/user/sign-up/verify/mobile/otp/"
const val createUserAccount = "auth/user/sign-up/"
const val forgotPassSendEmail = "auth/user/forgot-password/send/email/otp/"
const val verifyEmailForgotPass = "auth/user/forgot-password/verify/email/otp/"
const val resetPassword = "auth/user/forgot-password/"
const val addOrganization = "organization/create/organization/"
const val addOrganizationSocial = "organization/about/info/organization/"

/**
 * Multipart API parameter key
 */

const val NAME = "name"
const val COUNTRY_ID = "country_id"


/**
 * Https status code
 */
const val BAD_REQUEST = 400
const val FORBIDDEN_ACCESS = 403
const val UNKNOWN_HOST_EXCEPTION = 500
const val HTTP_SUCCESS = 200
const val HTTP_CREATED = 201
const val HTTP_ACCEPTED = 202
const val HTTP_SERVICE_UNAVAILABLE = 503
const val HTTP_PARSE_RESPONSE = 505
const val TIMEOUT_30_SEC = 30L
const val TIMEOUT_60_SEC = 60L

/**
 * Rest JSON Keys
 */
const val CLIENT_ID = "client_id"
const val CLIENT_SECRET = "client_secret"
const val GRANT_TYPE = "grant_type"
const val SCOPE = "SCOPE"
const val ACCESS_TOKEN = "access_token"
const val TOKEN_TYPE = "token_type"
const val EXPIRES_IN = "expires_in"
const val USER_NAME = "username"
const val PASSWORD = "password"
const val REFRESH_TOKEN = "refresh_token"
const val USER_ID = "userId"
const val TOKEN = "token"
const val IS_CUSTOMER = "isCustomer"
const val CURRENT_PAGE = "currentPage"
const val FIELDS = "fields"
const val SEARCH_QUERY = "query"
const val JOB_ID = "jobId"
const val PAGE_SIZE = "pageSize"
const val SORT = "sort"
const val CURRENT_PAGE_INDEX: Int = 0
const val DEFAULT_PAGE_SIZE: Int = 10
const val DEFAULT_ORDER_PAGE_SIZE: Int = 15
const val PLP_PAGE_SIZE: Int = 15
const val FIELDS_DEFAULT = "DEFAULT"
const val FIELDS_FULL = "FULL"
const val ZIP_CODE = "zipCode"
const val REGION = "region"
const val IS_CLEAR_JOB = "isClearJob"
const val ADDRESS = "address"
const val IS_CART_OR_CHECK_OUT = "isCartOrCheckout"
const val POS_NAME = "posName"
const val UPDATED_ASC = "updatedAsc"
const val BRANCH_LIST_UI_PAGE_SIZE: Int = 5
const val PRODUCT_CODE = "productCode"
const val START_DATE = "startDate"
const val END_DATE = "endDate"
const val CODE = "code"
const val QUANTITY = "quantity"
const val UOM = "uom"
const val SERVICE_NOT_AVAILABLE_MSG = "service not available"
const val PARSING_ERROR_MSG = "error parsing json"
const val LIST_CART_CODES = "listCartCodes"
const val PRODUCT_QUANTITY = "qty"
const val JOB_CODE = "jobCode"
const val PIN_JOB = "pinJob"
const val STATUS = "status"
const val SEARCH_TEXT = "searchText"
const val FILE = "file"
const val FILES = "files"

const val STATE = "state"
const val COUNTRY_KEY = "country"

/**
 * @PRODUCT_SPECS_URL key need to pass as to get product specification
 * */
const val PRODUCT_SPECS_URL = "productSpecsUrl"

/**
 * Get Wish List const
 */
const val WISH_LIST_SORT = "sort"
const val WISH_LIST_CURRENT_PAGE_INDEX: Int = 0
const val WISH_LIST_FIELDS_DEFAULT = "DEFAULT"
const val WISH_LIST_DEFAULT_PAGE_SIZE: Int = 20

/**
 * Constants for to handle login error cases
 */
const val ERROR_CODE_FBM01 = "FBM01"
const val ERROR_CODE_FBM02 = "FBM02"
const val SPLIT_CHAR = "|"
const val LOGIN_ERROR_TYPE_INDEX = 0
const val LOGIN_ERROR_MSG_INDEX = 1
const val LOGIN_ERROR_SUB_MSG_INDEX = 2
const val ONE = 1
const val TWO = 2

/**
 * Constants for Cart API
 */
const val ENTRY_NUMBER = "entryNumber"
const val CART_QUANTITY = "quantity"
const val CART_NOTES = "lineNotes"
const val CART_VOUCHER_CODE = "voucherCode"

/**
 * This class is used to define grant type for REST parameter
 */
sealed class GrantType(val type: String) {
    class ClientCredentials : GrantType("client_credentials")
    class Password : GrantType("password")

    class RefreshToken : GrantType("refresh_token")
}

sealed class User(val user: String) {
    class Current : User("current")
    class Anonymous : User("anonymous")
}
