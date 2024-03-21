package com.example.ticketpartner.common.di

import com.example.ticketpartner.feature_create_event.data.datasource.CreateEventDataSourceImpl
import com.example.ticketpartner.feature_create_event.data.repository.CreateEventRepositoryImpl
import com.example.ticketpartner.feature_create_event.domain.datasource.CreateEventDataSource
import com.example.ticketpartner.feature_create_event.domain.repository.CreateEventRepository
import com.example.ticketpartner.feature_create_event.domain.useCase.GetCreateEventTicketListUseCase
import com.example.ticketpartner.feature_create_event.domain.useCase.GetTimeZoneUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@InstallIn(SingletonComponent::class)
@Module
object CreateEventModule {
    @Provides
    fun getCreateEventRepository(impl: CreateEventRepositoryImpl): CreateEventRepository = impl

    @Provides
    fun getCreateEventDataSource(impl: CreateEventDataSourceImpl): CreateEventDataSource = impl

    @Provides
    fun getTimeZoneUseCase(impl: CreateEventRepositoryImpl): GetTimeZoneUseCase =
        GetTimeZoneUseCase(impl)

    @Provides
    fun getTicketList(impl: CreateEventRepositoryImpl): GetCreateEventTicketListUseCase =
        GetCreateEventTicketListUseCase(impl)

}