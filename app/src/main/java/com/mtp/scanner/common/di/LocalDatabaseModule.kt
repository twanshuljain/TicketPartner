package com.mtp.scanner.common.di

import android.content.Context
import androidx.room.Room
import com.mtp.scanner.common.TP_LOCAL_DATABASE
import com.mtp.scanner.common.localDatabase.MIGRATION_1_2
import com.mtp.scanner.common.localDatabase.TPLocalDatabase
import com.mtp.scanner.common.localDatabase.TpScanDao
import com.mtp.scanner.feature_local_storage.data.datasource.LocalStorageDataSourceImpl
import com.mtp.scanner.feature_local_storage.data.repository.LocalStorageRepositoryImpl
import com.mtp.scanner.feature_local_storage.domain.datasourse.LocalStorageDataSource
import com.mtp.scanner.feature_local_storage.domain.repository.LocalStorageRepository
import com.mtp.scanner.feature_scan_module.feature_login_scan.domain.repository.LoginScanRepository
import com.mtp.scanner.feature_scan_module.feature_login_scan.domain.usecase.GetQrCodeListUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object LocalDatabaseModule {
    @Provides
    @Singleton
    fun providesAppLocalDatabase(@ApplicationContext appContext: Context): TPLocalDatabase =
        Room.databaseBuilder(
            appContext,
            TPLocalDatabase::class.java,
            TP_LOCAL_DATABASE
        ).fallbackToDestructiveMigration().build()

    @Provides
    fun providesRandomQuotesDao(tpLocalDatabase: TPLocalDatabase): TpScanDao =
        tpLocalDatabase.tpScanDao()

    @Provides
    fun provideLocalStorageRepositoryImpl(repositoryImpl: LocalStorageRepositoryImpl): LocalStorageRepository =
        repositoryImpl

    @Provides
    fun provideLocalStorageDataSourceImpl(localStorageDataSourceImpl: LocalStorageDataSourceImpl): LocalStorageDataSource =
        localStorageDataSourceImpl

    @Provides
    fun provideGetQrCodeListForOfflineScanUseCase(loginScanRepository: LoginScanRepository): GetQrCodeListUseCase =
        GetQrCodeListUseCase(loginScanRepository)

}