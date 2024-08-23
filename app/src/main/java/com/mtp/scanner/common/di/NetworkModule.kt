package com.mtp.scanner.common.di

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import com.mtp.scanner.BuildConfig
import com.mtp.scanner.common.LogUtil
import com.mtp.scanner.common.remote.apis.RestApiService
import com.mtp.scanner.common.remote.apis.SessionHandlerInterceptor
import com.mtp.scanner.common.remote.apis.SessionManager
import com.mtp.scanner.common.remote.apis.TIMEOUT_60_SEC
import com.technotoil.ticket.common.storage.UserPreference
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

/**
 * Network module. Maintains dependencies for network related classes
 */
@InstallIn(SingletonComponent::class)
@Module
class NetworkModule {

    @Provides
    fun providesBaseUrl(): String = BuildConfig.API_URL


    @Singleton
    @Provides
    fun providesHttpLoggingInterceptor() = HttpLoggingInterceptor()
        .apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

    @Singleton
    @Provides
    fun provideOkHttpClient(
        sessionManager: SessionManager, logUtil: LogUtil
    ) =
        if (BuildConfig.DEBUG) {
            val loggingInterceptor = HttpLoggingInterceptor()
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
            OkHttpClient.Builder()
                .connectTimeout(TIMEOUT_60_SEC, TimeUnit.SECONDS)
                .readTimeout(TIMEOUT_60_SEC, TimeUnit.SECONDS)
                .writeTimeout(TIMEOUT_60_SEC, TimeUnit.SECONDS)
                .addInterceptor(
                    SessionHandlerInterceptor(
                        sessionManager = sessionManager,
                        logUtil = logUtil
                    )
                )
                .addInterceptor(loggingInterceptor)
                .build()
        } else {
            OkHttpClient.Builder()
                .connectTimeout(TIMEOUT_60_SEC, TimeUnit.SECONDS)
                .readTimeout(TIMEOUT_60_SEC, TimeUnit.SECONDS)
                .writeTimeout(TIMEOUT_60_SEC, TimeUnit.SECONDS)
                .addInterceptor(
                    SessionHandlerInterceptor(
                        sessionManager = sessionManager,
                        logUtil = logUtil
                    )
                )
                .build()
        }

    @Singleton
    @Provides
    fun provideRetrofit(baseUrl: String, okHttpClient: OkHttpClient): Retrofit =
        Retrofit.Builder().addConverterFactory(GsonConverterFactory.create())
            .baseUrl(baseUrl).client(okHttpClient).build()

    @Provides
    @Singleton
    fun provideApi(retrofit: Retrofit): RestApiService = retrofit.create(RestApiService::class.java)

    @Provides
    @Singleton
    fun provideSessionManager(
        userPreference: UserPreference,
        context: Application
    ) = SessionManager(
        userPreference,
        context
    )

    @Provides
    @Singleton
    fun provideConnectivityManager(application: Application): ConnectivityManager {
        return application.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    }

 /*   @Provides
    @Singleton
    fun provideNetworkMonitor(application: Application, connectivityManager: ConnectivityManager): NetworkMonitor {
        return NetworkMonitor(application, connectivityManager)
    }
*/
}