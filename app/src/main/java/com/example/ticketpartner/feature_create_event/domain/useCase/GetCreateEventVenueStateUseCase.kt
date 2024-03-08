package com.example.ticketpartner.feature_create_event.domain.useCase

import com.example.ticketpartner.feature_create_event.domain.model.CreateEventVenueStateResponse
import com.example.ticketpartner.feature_create_event.domain.repository.CreateEventRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class GetCreateEventVenueStateUseCase @Inject constructor(private val createEventRepository: CreateEventRepository) {
    suspend fun invoke(countryId: Int): Flow<CreateEventVenueStateResponse> {
        return flow {
            emit(createEventRepository.getStateBasedOnCountry(countryId))
        }.flowOn(Dispatchers.IO)
    }
}