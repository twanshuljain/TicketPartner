package com.mtp.scanner.common.di

import com.mtp.scanner.feature_add_organization.data.datasource.AddOrganizationDataSourceImpl
import com.mtp.scanner.feature_add_organization.data.repository.AddOrganizationRepositoryImpl
import com.mtp.scanner.feature_add_organization.domain.datasource.AddOrganizationDataSource
import com.mtp.scanner.feature_add_organization.domain.repository.AddOrganizationRepository
import com.mtp.scanner.feature_add_organization.domain.usecase.GetAddOrgSocialUseCase
import com.mtp.scanner.feature_add_organization.domain.usecase.GetAddOrganizationUseCase
import com.mtp.scanner.feature_add_organization.domain.usecase.GetSearchCountryUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@InstallIn(SingletonComponent::class)
@Module
object AddOrganizationModule {

    @Provides
    fun getAddOrganizationRepository(impl: AddOrganizationRepositoryImpl): AddOrganizationRepository =
        impl

    @Provides
    fun getAddOrganizationDataSource(impl: AddOrganizationDataSourceImpl): AddOrganizationDataSource =
        impl

    @Provides
    fun getAddOrganizationUseCase(impl: AddOrganizationRepository): GetAddOrganizationUseCase =
        GetAddOrganizationUseCase(impl)

    @Provides
    fun getAddOrganizationSocialUseCase(impl: AddOrganizationRepository): GetAddOrgSocialUseCase =
        GetAddOrgSocialUseCase(impl)

    @Provides
    fun getAddOrganizationSearCountryUseCase(impl: AddOrganizationRepository): GetSearchCountryUseCase =
        GetSearchCountryUseCase(impl)
}