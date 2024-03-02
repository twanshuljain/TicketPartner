package com.example.ticketpartner.feature_signup.domain.usecase

import com.example.ticketpartner.feature_signup.domain.model.CreateUserAccountRequest
import com.example.ticketpartner.feature_signup.domain.model.CreateUserAccountResponse
import com.example.ticketpartner.feature_signup.domain.repository.SignUpRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class GetCreateUserAccountUseCase @Inject constructor(private val signUpRepository: SignUpRepository) {
    suspend fun invoke(createUserAccountRequest: CreateUserAccountRequest): Flow<CreateUserAccountResponse> {
        return flow {
            emit(signUpRepository.createUserAccount(createUserAccountRequest))
        }.flowOn(Dispatchers.IO)
    }
}