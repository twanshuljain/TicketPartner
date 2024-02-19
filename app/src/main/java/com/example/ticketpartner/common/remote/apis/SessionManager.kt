package com.example.ticketpartner.common.remote.apis

import android.content.Context
import com.example.ticketpartner.BuildConfig
import com.example.ticketpartner.common.remote.model.TokenResponse
import com.technotoil.ticket.common.storage.UserPreference
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Inject

/**
 * Session manager handling token expiry scenarios
 *
 * @property userPreference
 * @constructor Create [SessionManager]
 */
@OptIn(DelicateCoroutinesApi::class)
class SessionManager @Inject constructor(
    private val userPreference: UserPreference,
    @ApplicationContext val context: Context
) {
    private val retrofitService = getRetrofitInstance()

    /**
     * Get login details. Gets saved login token from user preference
     *
     * @return [TokenResponse]
     */
    fun getLoginDetails(): TokenResponse = userPreference.getUserSession()

    /**
     * Is user logged in. Checks if user preference has stored customer token
     *
     * @return
     */
    fun isUserLoggedIn(): Boolean {
        var isUserLoggedIn = ""
        GlobalScope.launch { userPreference.getUserLoggedIn.collect { isUserLoggedIn = it } }
        return isUserLoggedIn != ""
    }

    /**
     * Flush user session. Deletes stored customer token from user preference.
     *
     * @return
     */
    fun flushUserSession() = userPreference.flushUserLoggedIn()

    /**
     * Get local retrofit instance to avoid cyclic dependency
     *
     * @return [RestApiService]
     */
    private fun getRetrofitInstance(): RestApiService {
        val retrofit = Retrofit.Builder().addConverterFactory(GsonConverterFactory.create())
            .baseUrl(BuildConfig.API_URL).client(
                OkHttpClient.Builder()
                    .build()
            ).build()
        return retrofit.create(RestApiService::class.java)
    }}