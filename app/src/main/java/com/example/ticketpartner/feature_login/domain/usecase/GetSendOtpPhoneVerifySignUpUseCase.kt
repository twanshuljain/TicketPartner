package com.example.ticketpartner.feature_login.domain.usecase

import com.example.ticketpartner.feature_login.domain.model.SendPhoneSignUpOtpResponse
import com.example.ticketpartner.feature_login.domain.model.SendPhoneSignUpOtpVerifyResponse
import com.example.ticketpartner.feature_login.domain.repository.LoginRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class GetSendOtpPhoneVerifySignUpUseCase @Inject constructor(private val loginRepository: LoginRepository) {
    suspend fun invoke(
        countryCode: String,
        phoneNumber: String,
        otp: String
    ): Flow<SendPhoneSignUpOtpVerifyResponse> {
        return flow {
            emit(loginRepository.sendPhoneOtpVerifySignUp(countryCode, phoneNumber, otp))
        }.flowOn(Dispatchers.IO)
    }
}