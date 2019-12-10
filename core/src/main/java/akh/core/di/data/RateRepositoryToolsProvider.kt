package akh.core.di.data

import akh.core.repository.RateRepository

interface RateRepositoryToolsProvider {

    fun provideRateRepository(): RateRepository

}