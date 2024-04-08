package com.example.ticketpartner.common.di

import com.example.ticketpartner.scan_module.data.datasource.LoginScanDataSourceImpl
import com.example.ticketpartner.scan_module.data.repository.LoginScanRepositoryImpl
import com.example.ticketpartner.scan_module.domain.datasource.LoginScanDataSource
import com.example.ticketpartner.scan_module.domain.repository.LoginScanRepository
import com.example.ticketpartner.scan_module.domain.usecase.GetLoginWithPinUseCase
import com.example.ticketpartner.scan_module.domain.usecase.GetScanEventDetailsUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@InstallIn(SingletonComponent::class)
@Module
object LoginScanModule {

    @Provides
    fun getLoginScanRepository(impl: LoginScanRepositoryImpl): LoginScanRepository = impl

    @Provides
    fun getLoginScanDataSource(impl: LoginScanDataSourceImpl): LoginScanDataSource = impl

    @Provides
    fun providesGetLoginWithPinUseCase(loginScanRepository: LoginScanRepository): GetLoginWithPinUseCase =
        GetLoginWithPinUseCase(loginScanRepository)

    @Provides
    fun providesGetScanEventDetailsUseCase(loginScanRepository: LoginScanRepository): GetScanEventDetailsUseCase =
        GetScanEventDetailsUseCase(loginScanRepository)
}