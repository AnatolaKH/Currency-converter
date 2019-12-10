package akh.core.di.domain

import akh.core.usecase.RateScreenUseCase

interface RateUseCaseToolsProvider {

    fun provideRateScreenUseCase(): RateScreenUseCase

}