package com.mtp.scanner.feature_login.domain.usecase

import com.mtp.scanner.feature_login.domain.model.VerifyMobileOtpResponse
import com.mtp.scanner.feature_login.domain.repository.LoginRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class GetSendOtpLoginUseCase @Inject constructor(private val loginRepository: LoginRepository) {
    suspend fun invoke(countryCode: String, number: String): Flow<VerifyMobileOtpResponse> {
        return flow {
            emit(loginRepository.sendOtpLogin(countryCode,number))
        }.flowOn(Dispatchers.IO)
    }
}