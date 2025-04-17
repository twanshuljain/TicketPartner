package com.mtp.ticketpartner.scanner.feature_create_event.domain.useCase

import com.mtp.ticketpartner.scanner.feature_create_event.domain.model.CreateEventTypesResponse
import com.mtp.ticketpartner.scanner.feature_create_event.domain.repository.CreateEventRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class GetCreateEventTypesUseCase @Inject constructor(private val createEventRepository: CreateEventRepository) {
    suspend fun invoke(): Flow<CreateEventTypesResponse> {
        return flow {
            emit(createEventRepository.getEventType())
        }.flowOn(Dispatchers.IO)
    }
}