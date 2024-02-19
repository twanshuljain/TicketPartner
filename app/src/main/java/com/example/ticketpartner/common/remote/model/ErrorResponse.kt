package com.example.ticketpartner.common.remote.model

import android.os.Parcelable
import com.example.ticketpartner.common.remote.apis.UNKNOWN_HOST_EXCEPTION
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize


/**
 * This class is used to parse api error response
 */
@Parcelize
data class ErrorResponse(
    @SerializedName("error")
    var error: String? = "",
    @SerializedName("error_description")
    var errorDescription: String? = "",
    var errorCode: Int = UNKNOWN_HOST_EXCEPTION,
    @SerializedName("errors")
    var errors: List<Errors>? = emptyList()
) : Parcelable