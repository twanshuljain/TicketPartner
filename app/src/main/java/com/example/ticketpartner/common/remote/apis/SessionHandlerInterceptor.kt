package com.example.ticketpartner.common.remote.apis

import com.example.ticketpartner.common.LogUtil
import com.example.ticketpartner.common.storage.MyPreferences
import com.example.ticketpartner.common.storage.PrefConstants
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import javax.inject.Inject

/**
 * Header interceptor. To add dynamic headers in the remote service calls.
 * Cache is being used to provide anonymousToken which is conditional based on few APIs
 * You can add cases here to distinguish use between anonymousToken and clientToken
 * Cache or dataStore can used to fetch respective tokens
 *
 * @constructor Create [SessionHandlerInterceptor]
 */
class SessionHandlerInterceptor @Inject constructor(
    private val sessionManager: SessionManager,
    private val logUtil: LogUtil
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        // Get access token based on login
        val accessToken = MyPreferences.getString(PrefConstants.ACCESS_TOKEN)
        // Regular API call with access token
        var response = chain.proceed(newRequestWithAccessToken(accessToken!!, request))
        return response
    }

    /**
     * New request with access token. This adds header to each api with saved access token
     *
     * @param accessToken Access token
     * @param request Request
     * @return [Request]
     */
    private fun newRequestWithAccessToken(accessToken: String, request: Request): Request {
        return request.newBuilder().addHeader(
            AUTHORIZATION, BEARER.plus(accessToken)
        ).addHeader(CONTENT_TYPE, APPLICATION_JSON).build()
    }

    companion object {
        val TAG: String = SessionHandlerInterceptor::class.java.simpleName
        const val REFRESH_TOKEN = "refresh"
        const val ACCESS_TOKEN = "Access token"
        const val AUTHORIZATION = "Authorization"
        const val BEARER = "Bearer "
        const val CONTENT_TYPE = "Content-Type"
        const val APPLICATION_JSON = "application/json"
    }
}