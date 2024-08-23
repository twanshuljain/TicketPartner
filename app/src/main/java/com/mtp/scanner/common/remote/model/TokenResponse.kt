package com.mtp.scanner.common.remote.model

import android.os.Parcelable
import com.mtp.scanner.common.remote.apis.REFRESH_TOKEN
import com.mtp.scanner.common.remote.apis.TOKEN_TYPE
import com.mtp.scanner.common.storage.PrefConstants.ACCESS_TOKEN
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

/**
 * Token Response will save response data received from API
 * */
@Parcelize
data class TokenResponse(
    @SerializedName(ACCESS_TOKEN)
    var accessToken: String?,
    @SerializedName(TOKEN_TYPE)
    var tokenType: String?,
    @SerializedName(REFRESH_TOKEN)
    var refreshToken: String? = null,
) : Parcelable