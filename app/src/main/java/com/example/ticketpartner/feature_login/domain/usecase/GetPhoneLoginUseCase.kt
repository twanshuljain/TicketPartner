package com.example.ticketpartner.feature_login.domain.usecase

import com.example.ticketpartner.feature_login.domain.model.UserLoginPhoneResponse
import com.example.ticketpartner.feature_login.domain.repository.LoginRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class GetPhoneLoginUseCase @Inject constructor(private val loginRepository: LoginRepository) {
    suspend fun invoke(
        countryCode: String,
        phoneNumber: String,
        otp: String
    ): Flow<UserLoginPhoneResponse> {
        return flow {
            emit(loginRepository.loginUserByPhone(countryCode, phoneNumber, otp))
        }.flowOn(Dispatchers.IO)
    }
}