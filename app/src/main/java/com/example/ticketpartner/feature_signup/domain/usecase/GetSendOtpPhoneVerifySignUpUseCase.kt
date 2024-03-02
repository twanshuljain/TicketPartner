package com.example.ticketpartner.feature_signup.domain.usecase

import com.example.ticketpartner.feature_signup.domain.model.SendPhoneSignUpOtpVerifyResponse
import com.example.ticketpartner.feature_signup.domain.repository.SignUpRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class GetSendOtpPhoneVerifySignUpUseCase @Inject constructor(private val signUpRepository: SignUpRepository) {
    suspend fun invoke(
        countryCode: String,
        phoneNumber: String,
        otp: String
    ): Flow<SendPhoneSignUpOtpVerifyResponse> {
        return flow {
            emit(signUpRepository.sendPhoneOtpVerifySignUp(countryCode, phoneNumber, otp))
        }.flowOn(Dispatchers.IO)
    }
}