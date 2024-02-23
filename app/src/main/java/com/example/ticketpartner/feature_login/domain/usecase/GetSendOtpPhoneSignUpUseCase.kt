package com.example.ticketpartner.feature_login.domain.usecase

import com.example.ticketpartner.feature_login.domain.model.SendPhoneSignUpOtpResponse
import com.example.ticketpartner.feature_login.domain.repository.LoginRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class GetSendOtpPhoneSignUpUseCase @Inject constructor(private val loginRepository: LoginRepository) {
    suspend fun invoke(
        countryCode: String,
        phoneNumber: String
    ): Flow<SendPhoneSignUpOtpResponse> {
        return flow {
            emit(loginRepository.sendPhoneOtpSignUp(countryCode,phoneNumber))
        }.flowOn(Dispatchers.IO)
    }
}