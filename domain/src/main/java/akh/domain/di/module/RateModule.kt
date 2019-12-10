package akh.domain.di.module

import akh.core.usecase.RateScreenUseCase
import akh.core.usecase.RateUpdateUseCase
import akh.core.usecase.RateUseCase
import akh.domain.usecase.rate.RateScreenUseCaseImpl
import akh.domain.usecase.rate.RateUpdateUseCaseImpl
import akh.domain.usecase.rate.RateUseCaseImpl
import dagger.Binds
import dagger.Module

@Suppress("unused")
@Module
interface RateModule {

    @Binds
    fun rateUseCase(rateUseCaseImpl: RateUseCaseImpl): RateUseCase

    @Binds
    fun rateUpdateUseCase(rateUpdateUseCaseImpl: RateUpdateUseCaseImpl): RateUpdateUseCase

    @Binds
    fun rateScreenUseCase(rateScreenUseCaseImpl: RateScreenUseCaseImpl): RateScreenUseCase

}