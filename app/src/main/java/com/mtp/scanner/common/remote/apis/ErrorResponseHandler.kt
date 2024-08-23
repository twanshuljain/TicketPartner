package com.technotoil.tglivescan.common.retrofit.apis

import com.mtp.scanner.common.remote.apis.BAD_REQUEST
import com.mtp.scanner.common.remote.apis.HTTP_SERVICE_UNAVAILABLE
import com.mtp.scanner.common.remote.apis.PARSING_ERROR_MSG
import com.mtp.scanner.common.remote.apis.SERVICE_NOT_AVAILABLE_MSG
import com.mtp.scanner.common.remote.apis.UNAUTHORIZED_USER
import com.google.gson.Gson
import com.technotoil.tglivescan.common.retrofit.model.ErrorResponse
import okhttp3.ResponseBody
import retrofit2.HttpException
import java.net.UnknownHostException

/**
 * This class is used to handle REST API Error response
 */
class ErrorResponseHandler constructor(it: Throwable) {
    private var errorResponse: ErrorResponse

    init {
        errorResponse = parseErrorResponse(it)
    }

    /**
     * This method is used to handle error code for UI side error message
     * @param it api error response
     */
    private fun parseErrorResponse(it: Throwable): ErrorResponse {
        var errorCode = ErrorResponse()
        when (it) {
            is HttpException -> {
                when (it.code()) {
                    HTTP_SERVICE_UNAVAILABLE -> {
                        errorCode = ErrorResponse(
                            BAD_REQUEST.toString(),
                            SERVICE_NOT_AVAILABLE_MSG
                        )
                    }

                    UNAUTHORIZED_USER -> {
                        errorCode = ErrorResponse(
                            status_code = UNAUTHORIZED_USER.toString(),
                            message = "Unauthorized. Please log in again."
                        )
                    }

                    else ->
                        it.response()?.let {
                            errorCode = createErrorResponse(it.errorBody(), it.code())
                        }
                }

            }

            is UnknownHostException -> errorCode = ErrorResponse()
        }
        return errorCode
    }

    /**
     * This method is used to create Error response to update UI
     * @param errorBody api error body
     * @param errorCode api error code
     */
    private fun createErrorResponse(errorBody: ResponseBody?, errorCode: Int): ErrorResponse {
        return try {
            var errorResponse = ErrorResponse()
            errorBody?.let {
                errorResponse = Gson().fromJson(it.string(), ErrorResponse::class.java)
            }
            errorResponse
        } catch (e: Exception) {
            ErrorResponse(status_code = errorCode.toString(), message = PARSING_ERROR_MSG)
        }
    }

    fun getErrors(): ErrorResponse {
        return errorResponse
    }

}