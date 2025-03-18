package com.mtp.ticketpartner.scanner.feature_login.domain.usecase

import com.mtp.ticketpartner.scanner.feature_login.domain.model.ResetPasswordResponse
import com.mtp.ticketpartner.scanner.feature_login.domain.repository.LoginRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class GetResetPasswordUseCase @Inject constructor(private val loginRepository: LoginRepository) {
    suspend fun invoke(
        newPassword: String,
        conPassword: String
    ): Flow<ResetPasswordResponse> {
        return flow {
            emit(loginRepository.resetPassword(newPassword,conPassword))
        }.flowOn(Dispatchers.IO)
    }
}