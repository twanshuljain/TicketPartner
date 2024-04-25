package com.example.ticketpartner.common.di

import com.example.ticketpartner.feature_scan_module.feature_login_scan.data.datasource.LoginScanDataSourceImpl
import com.example.ticketpartner.feature_scan_module.feature_login_scan.data.repository.LoginScanRepositoryImpl
import com.example.ticketpartner.feature_scan_module.feature_login_scan.domain.datasource.LoginScanDataSource
import com.example.ticketpartner.feature_scan_module.feature_login_scan.domain.repository.LoginScanRepository
import com.example.ticketpartner.feature_scan_module.feature_login_scan.domain.usecase.GetLoginWithPinUseCase
import com.example.ticketpartner.feature_scan_module.feature_login_scan.domain.usecase.GetScanEventDetailsUseCase
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