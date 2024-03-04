package com.example.ticketpartner.feature_login.domain.usecase

import com.example.ticketpartner.feature_login.domain.model.VerifyEmailForgotPassResponse
import com.example.ticketpartner.feature_login.domain.repository.LoginRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class GetVerifyEmailForgotPassUseCase @Inject constructor(private val loginRepository: LoginRepository) {
    suspend fun invoke(
        email: String,
        otp: String
    ): Flow<VerifyEmailForgotPassResponse> {
        return flow {
            emit(loginRepository.verifyEmailForgotPass(email, otp))
        }.flowOn(Dispatchers.IO)
    }
}