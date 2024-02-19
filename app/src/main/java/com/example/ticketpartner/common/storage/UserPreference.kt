package com.technotoil.ticket.common.storage

import android.content.Context
import android.util.ArrayMap
import com.example.ticketpartner.common.EMPTY_STRING
import com.example.ticketpartner.common.PASSWORD
import com.example.ticketpartner.common.USER_NAME
import com.example.ticketpartner.common.remote.model.TokenResponse
import com.technotoil.ticket.common.storage.UserPreference.UserPreferenceKeys.ACCESS_TOKEN
import com.technotoil.ticket.common.storage.UserPreference.UserPreferenceKeys.MODE
import com.technotoil.ticket.common.storage.UserPreference.UserPreferenceKeys.PASSWORD_KEY
import com.technotoil.ticket.common.storage.UserPreference.UserPreferenceKeys.PREFERENCE_NAME
import com.technotoil.ticket.common.storage.UserPreference.UserPreferenceKeys.REFRESH_TOKEN
import com.technotoil.ticket.common.storage.UserPreference.UserPreferenceKeys.TOKEN_TYPE
import com.technotoil.ticket.common.storage.UserPreference.UserPreferenceKeys.USER_EMAIL
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

/**
 * This class is used to save user session with encryption
 * Key scheme : AES 256 GCM
 */
class UserPreference @Inject constructor(@ApplicationContext val context: Context) {

    /**
     * Keys to access storage preference
     */
    private object UserPreferenceKeys {
        const val ALIAS_NAME = "UserKey"
        const val PREFERENCE_NAME = "User"
        const val ACCESS_TOKEN = "access_token"
        const val TOKEN_TYPE = "token_type"
        const val REFRESH_TOKEN = "refresh_token"
        const val SCOPE = "scope"
        const val USER_EMAIL = "email"
        const val PASSWORD_KEY = "Password"
        const val MODE = Context.MODE_PRIVATE
    }

    /**
     * Reference to access shared preference
     */
    private val sharedPreferences =  context.getSharedPreferences(
        PREFERENCE_NAME, MODE
    )

    val getUserLoggedIn: Flow<String> = getAccessToken()

    /**
     * Flush user logged in. Basically clears access token specifically
     *
     * @return
     */
    fun flushUserLoggedIn() = sharedPreferences.edit().apply {
        putString(ACCESS_TOKEN, EMPTY_STRING)
    }.apply()

    /**
     * This method is used to get access token from preference
     */
    private fun getAccessToken(): Flow<String> {
        return flow {
            emit(sharedPreferences.getString(ACCESS_TOKEN, EMPTY_STRING) ?: EMPTY_STRING)
        }
    }

    val getUserData: Flow<TokenResponse> = getLoginResponse()

    /**
     * This method is used to get login response from preference
     */
    private fun getLoginResponse(): Flow<TokenResponse> {
        return flow {
            emit(getUserSession())
        }
    }

    /**
     * This method is used to save user session in preference
     * @param tokenResponse session to store
     */
    fun updateUserLogin(tokenResponse: TokenResponse) {
        sharedPreferences.edit().apply {
            putString(ACCESS_TOKEN, tokenResponse.accessToken)
            putString(TOKEN_TYPE, tokenResponse.tokenType)
            putString(REFRESH_TOKEN, tokenResponse.refreshToken)
        }.apply()

    }

    /**
     * This method is used to clear user session from preference
     */
    fun logout() {
        sharedPreferences.edit().apply {
            putString(ACCESS_TOKEN, EMPTY_STRING)
            putString(TOKEN_TYPE, EMPTY_STRING)
            putString(REFRESH_TOKEN, EMPTY_STRING)
        }.apply()
    }

    /**
     * This method is used to save email and password for biometric authentication
     * @param email email id
     * @param password password
     */
    fun saveUserDetails(email: String, password: String) {
        sharedPreferences.edit().apply {
            putString(USER_EMAIL, email)
            putString(PASSWORD_KEY, password)
        }.apply()
    }

    fun getUserDetails(): Map<String, String> {
        val map = ArrayMap<String, String>()
        map[USER_NAME] = sharedPreferences.getString(USER_EMAIL, EMPTY_STRING)
        map[PASSWORD] = sharedPreferences.getString(PASSWORD_KEY, EMPTY_STRING)
        return map
    }

    fun getUserSession(): TokenResponse {
        val accessToken =
            sharedPreferences.getString(ACCESS_TOKEN, EMPTY_STRING) ?: EMPTY_STRING
        val tokenType =
            sharedPreferences.getString(TOKEN_TYPE, EMPTY_STRING) ?: EMPTY_STRING
        val refreshToken =
            sharedPreferences.getString(REFRESH_TOKEN, EMPTY_STRING) ?: EMPTY_STRING
        return TokenResponse(accessToken, tokenType, refreshToken)
    }

}