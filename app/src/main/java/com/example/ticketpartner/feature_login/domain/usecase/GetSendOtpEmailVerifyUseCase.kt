package com.example.ticketpartner.feature_login.domain.usecase

import com.example.ticketpartner.feature_signup.domain.model.SendEmailOtpVerifyResponse
import com.example.ticketpartner.feature_signup.domain.repository.SignUpRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class GetSendOtpEmailVerifyUseCase @Inject constructor(private val signUpRepository: SignUpRepository) {
    suspend fun invoke(
        email: String, otpNumber: String
    ): Flow<SendEmailOtpVerifyResponse> {
        return flow {
            emit(signUpRepository.sendEmailOtpVerify(email, otpNumber))
        }.flowOn(Dispatchers.IO)
    }
}