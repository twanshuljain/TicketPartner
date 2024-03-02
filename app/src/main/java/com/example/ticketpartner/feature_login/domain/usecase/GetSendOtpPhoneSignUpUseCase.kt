package com.example.ticketpartner.feature_login.domain.usecase

import com.example.ticketpartner.feature_signup.domain.model.SendPhoneSignUpOtpResponse
import com.example.ticketpartner.feature_signup.domain.repository.SignUpRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class GetSendOtpPhoneSignUpUseCase @Inject constructor(private val signUpRepository: SignUpRepository) {
    suspend fun invoke(
        countryCode: String,
        phoneNumber: String
    ): Flow<SendPhoneSignUpOtpResponse> {
        return flow {
            emit(signUpRepository.sendPhoneOtpSignUp(countryCode, phoneNumber))
        }.flowOn(Dispatchers.IO)
    }
}