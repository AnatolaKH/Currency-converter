package akh.data.di.module

import akh.core.repository.RateRepository
import akh.data.repository.RateRepositoryImpl
import dagger.Binds
import dagger.Module

@Suppress("unused")
@Module
interface RateModule {

    @Binds
    fun rateRepository(rateRepositoryImpl: RateRepositoryImpl): RateRepository
}