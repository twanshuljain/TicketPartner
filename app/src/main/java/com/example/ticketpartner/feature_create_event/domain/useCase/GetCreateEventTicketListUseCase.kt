package com.example.ticketpartner.feature_create_event.domain.useCase

import com.example.ticketpartner.feature_create_event.domain.model.CreateEventTicketListResponse
import com.example.ticketpartner.feature_create_event.domain.repository.CreateEventRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject


class GetCreateEventTicketListUseCase @Inject constructor(private val createEventRepository: CreateEventRepository) {
    suspend fun invoke(eventId: Int): Flow<CreateEventTicketListResponse> {
        return flow {
            emit(createEventRepository.getTicketList(eventId))
        }.flowOn(Dispatchers.IO)
    }
}