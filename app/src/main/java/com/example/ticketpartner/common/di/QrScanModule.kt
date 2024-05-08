package com.example.ticketpartner.common.di

import com.example.ticketpartner.feature_scan_module.feature_qr_scan.data.datasource.QrScanDataSourceImpl
import com.example.ticketpartner.feature_scan_module.feature_qr_scan.data.repository.QrScanRepositoryImpl
import com.example.ticketpartner.feature_scan_module.feature_qr_scan.domain.datasource.QrScanDataSource
import com.example.ticketpartner.feature_scan_module.feature_qr_scan.domain.repository.QrScanRepository
import com.example.ticketpartner.feature_scan_module.feature_qr_scan.domain.usecase.GetQrScanSearchUseCase
import com.example.ticketpartner.feature_scan_module.feature_qr_scan.domain.usecase.GetQrScanUseCase
import com.example.ticketpartner.feature_scan_module.feature_qr_scan.domain.usecase.GetScanEventDetailsDashboardUseCase
import com.example.ticketpartner.feature_scan_module.feature_qr_scan.domain.usecase.GetScanOrderDetailsUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@InstallIn(SingletonComponent::class)
@Module
object QrScanModule {

    @Provides
    fun getQrScanRepository(impl: QrScanRepositoryImpl): QrScanRepository = impl

    @Provides
    fun getQrScanDataSource(impl: QrScanDataSourceImpl): QrScanDataSource = impl

    @Provides
    fun providesGetUseCase(qrScanRepository: QrScanRepository): GetQrScanUseCase =
        GetQrScanUseCase(qrScanRepository)

    @Provides
    fun providesGetEventDetailsUseCase(qrScanRepository: QrScanRepository): GetScanEventDetailsDashboardUseCase =
        GetScanEventDetailsDashboardUseCase(qrScanRepository)

    @Provides
    fun providesSearchItemByOrderIdUseCase(qrScanRepository: QrScanRepository): GetQrScanSearchUseCase =
        GetQrScanSearchUseCase(qrScanRepository)

    @Provides
    fun providesSearchOrderDetailsUseCase(qrScanRepository: QrScanRepository): GetScanOrderDetailsUseCase =
        GetScanOrderDetailsUseCase(qrScanRepository)
}