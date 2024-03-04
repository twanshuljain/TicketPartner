package com.example.ticketpartner.feature_login.domain.usecase

import com.example.ticketpartner.feature_login.domain.model.SendEmailLinkForgotPasswordResponse
import com.example.ticketpartner.feature_login.domain.repository.LoginRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class GetSendEmailLinkUseCase @Inject constructor(private val loginRepository: LoginRepository) {
    suspend fun invoke(email: String): Flow<SendEmailLinkForgotPasswordResponse> {
        return flow {
            emit(loginRepository.forgotPasswordSendEmailLink(email))
        }.flowOn(Dispatchers.IO)
    }
}