package com.example.ticketpartner.common.remote.model


/**
 * This class is used to handle refresh token and access token fail and success case
 */
sealed class TokenResponseState {
    /**
     * OnSuccess to pass token response
     */
    data class OnSuccess(val tokenResponse: TokenResponse) : TokenResponseState()

    /**
     * OnError to pass errors to handle retry
     */
    data class OnError(val errorResponse: ErrorResponse) : TokenResponseState()
}