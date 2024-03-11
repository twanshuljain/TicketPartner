package com.example.ticketpartner.feature_create_event.domain.useCase

import com.example.ticketpartner.feature_create_event.domain.model.CreateEventGetTimeZoneResponse
import com.example.ticketpartner.feature_create_event.domain.repository.CreateEventRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class GetTimeZoneUseCase @Inject constructor(private val createEventRepository: CreateEventRepository) {
    suspend fun invoke(): Flow<CreateEventGetTimeZoneResponse> {
        return flow {
            emit(createEventRepository.getTimeZone())
        }.flowOn(Dispatchers.IO)
    }
}