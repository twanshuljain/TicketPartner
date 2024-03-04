package com.technotoil.tglivescan.common.retrofit.model

import com.google.gson.annotations.SerializedName


/**
 * This class is used to parse api error response
 */
data class ErrorResponse(
    @SerializedName("data")
    val data: Any? = null,
    @SerializedName("status_code")
    var status_code: String? = "",
    @SerializedName("message")
    var message: String? = "",
    @SerializedName("error")
    var error: Any? = null,
)