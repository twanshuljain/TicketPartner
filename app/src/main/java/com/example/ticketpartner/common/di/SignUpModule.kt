package com.example.ticketpartner.common.di

import com.example.ticketpartner.feature_login.domain.usecase.GetSendOtpEmailVerifyUseCase
import com.example.ticketpartner.feature_login.domain.usecase.GetSendOtpPhoneSignUpUseCase
import com.example.ticketpartner.feature_signup.data.datasource.SignUpDataSourceImpl
import com.example.ticketpartner.feature_signup.data.repository.SignUpRepositoryImpl
import com.example.ticketpartner.feature_signup.domain.datasource.SignUpDataSource
import com.example.ticketpartner.feature_signup.domain.repository.SignUpRepository
import com.example.ticketpartner.feature_signup.domain.usecase.GetCreateUserAccountUseCase
import com.example.ticketpartner.feature_signup.domain.usecase.GetSendOtpEmailSignUpUseCase
import com.example.ticketpartner.feature_signup.domain.usecase.GetSendOtpPhoneVerifySignUpUseCase
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
object SignUpModule {

    @Provides
    fun getSignUpRepository(impl: SignUpRepositoryImpl): SignUpRepository = impl

    @Provides
    fun getSignUpDataSource(impl: SignUpDataSourceImpl): SignUpDataSource = impl

    @Singleton
    @Provides
    fun providesSendEmailOtpSignUpUseCase(signUpRepository: SignUpRepository): GetSendOtpEmailSignUpUseCase =
        GetSendOtpEmailSignUpUseCase(signUpRepository)

    @Singleton
    @Provides
    fun providesSendEmailOtpVerifySignUpUseCase(signUpRepository: SignUpRepository): GetSendOtpEmailVerifyUseCase =
        GetSendOtpEmailVerifyUseCase(signUpRepository)

    @Singleton
    @Provides
    fun providesSendPhoneOtpSignUpUseCase(signUpRepository: SignUpRepository): GetSendOtpPhoneSignUpUseCase =
        GetSendOtpPhoneSignUpUseCase(signUpRepository)

    @Singleton
    @Provides
    fun providesSendPhoneOtpVerifySignUpUseCase(signUpRepository: SignUpRepository): GetSendOtpPhoneVerifySignUpUseCase =
        GetSendOtpPhoneVerifySignUpUseCase(signUpRepository)

    @Singleton
    @Provides
    fun providesCreateAccountSignUpUseCase(signUpRepository: SignUpRepository): GetCreateUserAccountUseCase =
        GetCreateUserAccountUseCase(signUpRepository)
}