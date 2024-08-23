package com.mtp.scanner.common.di

import com.mtp.scanner.feature_login.data.datasource.LoginDataSourceImpl
import com.mtp.scanner.feature_login.data.repository.LoginRepositoryImpl
import com.mtp.scanner.feature_login.domain.datasource.LoginDataSource
import com.mtp.scanner.feature_login.domain.repository.LoginRepository
import com.mtp.scanner.feature_login.domain.usecase.GetEmailLoginUseCase
import com.mtp.scanner.feature_login.domain.usecase.GetPhoneLoginUseCase
import com.mtp.scanner.feature_login.domain.usecase.GetSendOtpLoginUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 *  Dedicated login module for creating scoped dependencies
 */
@InstallIn(SingletonComponent::class)
@Module
object LoginModule {

    @Provides
    fun getLoginRepository(impl: LoginRepositoryImpl): LoginRepository = impl

    @Provides
    fun getLoginDataSource(impl: LoginDataSourceImpl): LoginDataSource = impl

    @Singleton
    @Provides
    fun providesGetEmailLoginUseCase(loginRepository: LoginRepository): GetEmailLoginUseCase =
        GetEmailLoginUseCase(loginRepository)

    @Singleton
    @Provides
    fun providesGetVerifyOtpUseCase(loginRepository: LoginRepository): GetSendOtpLoginUseCase =
        GetSendOtpLoginUseCase(loginRepository)

    @Singleton
    @Provides
    fun providesGetPhoneLoginUseCase(loginRepository: LoginRepository): GetPhoneLoginUseCase =
        GetPhoneLoginUseCase(loginRepository)

}