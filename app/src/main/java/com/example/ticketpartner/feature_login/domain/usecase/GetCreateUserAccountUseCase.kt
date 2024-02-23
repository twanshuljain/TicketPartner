package com.example.ticketpartner.feature_login.domain.usecase

import com.example.ticketpartner.feature_login.domain.model.CreateUserAccountRequest
import com.example.ticketpartner.feature_login.domain.model.CreateUserAccountResponse
import com.example.ticketpartner.feature_login.domain.repository.LoginRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class GetCreateUserAccountUseCase @Inject constructor(private val loginRepository: LoginRepository)  {
    suspend fun invoke(createUserAccountRequest: CreateUserAccountRequest): Flow<CreateUserAccountResponse> {
        return flow {
            emit(loginRepository.createUserAccount(createUserAccountRequest))
        }.flowOn(Dispatchers.IO)
    }
}