package com.android.algorithms.utils.di

import com.android.algorithms.data.repository.AlgorithmDetailsRepository
import com.android.algorithms.data.repository.AlgorithmDetailsRepositoryImpl
import com.android.algorithms.data.repository.AlgorithmListRepository
import com.android.algorithms.data.repository.AlgorithmListRepositoryImpl
import com.android.algorithms.data.repository.AlgorithmResultRepository
import com.android.algorithms.data.repository.AlgorithmResultRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

// Binds module

/**
 * Dagger module for providing repository implementations.
 */
@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    /**
     * Binds the implementation of AlgorithmListRepository.
     */
    @Binds
    @Singleton
    abstract fun bindAlgorithmListRepository(implementation: AlgorithmListRepositoryImpl): AlgorithmListRepository

    /**
     * Binds the implementation of AlgorithmDetailsRepository.
     */
    @Binds
    @Singleton
    abstract fun bindAlgorithmDetailsRepository(implementation: AlgorithmDetailsRepositoryImpl): AlgorithmDetailsRepository

    /**
     * Binds the implementation of AlgorithmResultRepository.
     */
    @Binds
    @Singleton
    abstract fun bindAlgorithmResultRepository(implementation: AlgorithmResultRepositoryImpl): AlgorithmResultRepository
}
