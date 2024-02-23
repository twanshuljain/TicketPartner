package com.example.ticketpartner.common.di

import android.os.CountDownTimer
import com.example.ticketpartner.common.LogUtil
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Module class : used for creating required dependency objects
 */
@InstallIn(SingletonComponent::class)
@Module
object AppModule {
    @Singleton
    @Provides
    fun provideLog(): LogUtil = LogUtil()

}