package com.example.ticketpartner.feature_login.domain.usecase

import com.example.ticketpartner.feature_login.domain.model.SendEmailOtpResponseSignUp
import com.example.ticketpartner.feature_login.domain.model.UserLoginPhoneResponse
import com.example.ticketpartner.feature_login.domain.repository.LoginRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class GetSendOtpEmailSignUpUseCase @Inject constructor(private val loginRepository: LoginRepository) {
    suspend fun invoke(
        email: String,
    ): Flow<SendEmailOtpResponseSignUp> {
        return flow {
            emit(loginRepository.sendEmailOtpSignUp(email))
        }.flowOn(Dispatchers.IO)
    }
}