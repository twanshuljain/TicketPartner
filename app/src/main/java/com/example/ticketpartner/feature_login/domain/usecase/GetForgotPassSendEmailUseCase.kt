package com.example.ticketpartner.feature_login.domain.usecase

import com.example.ticketpartner.feature_login.domain.model.ForgotPassSendEmailResponse
import com.example.ticketpartner.feature_login.domain.model.UserLoginResponse
import com.example.ticketpartner.feature_login.domain.repository.LoginRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class GetForgotPassSendEmailUseCase @Inject constructor(private val loginRepository: LoginRepository)  {
    suspend fun invoke(email: String): Flow<ForgotPassSendEmailResponse> {
        return flow {
            emit(loginRepository.forgotPasswordSendEmail(email))
        }.flowOn(Dispatchers.IO)
    }
}