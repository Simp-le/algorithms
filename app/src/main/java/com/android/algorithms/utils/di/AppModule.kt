package com.android.algorithms.utils.di

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import androidx.room.Room
import com.android.algorithms.data.local.AlgorithmDatabase
import com.android.algorithms.data.remote.AlgorithmApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

// Data module

/**
 * Dagger Hilt module for providing application-wide dependencies.
 */
@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    private val interceptor: HttpLoggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    private val client: OkHttpClient = OkHttpClient.Builder().addInterceptor(interceptor).build()

    // Dependencies
    /**
     * Provides an instance of the AlgorithmApi interface.
     */
    @Provides
    @Singleton
    fun providesAlgorithmApi(): AlgorithmApi {
        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(AlgorithmApi.BASE_URL)
            .client(client)
            .build()
            .create(AlgorithmApi::class.java)
    }

    /**
     * Provides an instance of the AlgorithmDatabase.
     */
    @Provides
    @Singleton
    fun providesAlgorithmDatabase(app: Application): AlgorithmDatabase {
        return Room.databaseBuilder(app, AlgorithmDatabase::class.java, "algorithm.db").build()
    }

    /**
     * Provides an instance of the ConnectivityManager.
     */
    @Provides
    @Singleton
    fun providesConnectivityManager(@ApplicationContext context: Context): ConnectivityManager {
        return context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    }

    /**
     * Provides the application context.
     */
    @Provides
    @Singleton
    fun providesApplicationContext(application: Application): Context {
        return application.applicationContext
    }
}
