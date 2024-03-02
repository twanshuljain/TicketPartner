package com.example.ticketpartner.feature_signup.domain.usecase

import com.example.ticketpartner.feature_signup.domain.model.SendEmailOtpSignUpResponse
import com.example.ticketpartner.feature_signup.domain.repository.SignUpRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class GetSendOtpEmailSignUpUseCase @Inject constructor(private val signUpRepository: SignUpRepository) {
    suspend fun invoke(
        email: String,
    ): Flow<SendEmailOtpSignUpResponse> {
        return flow {
            emit(signUpRepository.sendEmailOtpSignUp(email))
        }.flowOn(Dispatchers.IO)
    }
}