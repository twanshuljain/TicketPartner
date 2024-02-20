package com.example.ticketpartner.common.remote.apis

import com.example.ticketpartner.common.remote.model.ErrorResponse
import com.google.gson.Gson
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
                    if (it.code() == HTTP_SERVICE_UNAVAILABLE)
                        errorCode = ErrorResponse(
                            SERVICE_NOT_AVAILABLE_MSG,
                            SERVICE_NOT_AVAILABLE_MSG,
                            HTTP_SERVICE_UNAVAILABLE
                        )
                    else
                        it.response()?.let {
                            errorCode = createErrorResponse(it.errorBody(), it.code())
                        }
                }
                is UnknownHostException -> errorCode = ErrorResponse(errorCode = UNKNOWN_HOST_EXCEPTION)
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
            var errorResponse = ErrorResponse(errorCode = errorCode)
            errorBody?.let {
                errorResponse = Gson().fromJson(it.string(), ErrorResponse::class.java)
                errorResponse.errorCode = errorCode
            }
            errorResponse
        } catch (e: Exception) {
            ErrorResponse(PARSING_ERROR_MSG, PARSING_ERROR_MSG, HTTP_PARSE_RESPONSE)
        }
    }

    fun getErrors(): ErrorResponse {
        return errorResponse
    }

}