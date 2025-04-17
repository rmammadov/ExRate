// In a file, for example, RepositoryModule.kt
package com.ahb.exrate.di

import com.ahb.exrate.repository.CurrencyRateRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {
    @Provides
    @Singleton
    fun provideCurrencyRateRepository(): CurrencyRateRepository {
        return CurrencyRateRepository()
    }
}
