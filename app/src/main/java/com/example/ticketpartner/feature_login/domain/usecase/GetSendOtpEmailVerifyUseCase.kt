package com.example.ticketpartner.feature_login.domain.usecase

import com.example.ticketpartner.feature_login.domain.model.SendEmailOtpResponseSignUp
import com.example.ticketpartner.feature_login.domain.model.SendEmailOtpVerifyResponse
import com.example.ticketpartner.feature_login.domain.repository.LoginRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class GetSendOtpEmailVerifyUseCase @Inject constructor(private val loginRepository: LoginRepository) {
    suspend fun invoke(
        email: String,otpNumber: String
    ): Flow<SendEmailOtpVerifyResponse> {
        return flow {
            emit(loginRepository.sendEmailOtpVerify(email,otpNumber))
        }.flowOn(Dispatchers.IO)
    }
}