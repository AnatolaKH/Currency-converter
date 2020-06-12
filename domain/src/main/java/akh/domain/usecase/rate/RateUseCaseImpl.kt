package akh.domain.usecase.rate

import akh.core.base.Failure
import akh.core.base.Result
import akh.core.model.ActualRatesModel
import akh.core.model.RateModel
import akh.core.model.RatesModel
import akh.core.repository.RateRepository
import akh.core.repository.SecretDBRepository
import akh.core.usecase.RateUseCase
import akh.domain.common.withIO
import akh.domain.usecase.BaseUseCase
import javax.inject.Inject

class RateUseCaseImpl @Inject constructor(
    private val rateRepository: RateRepository,
    private val secretDBRepository: SecretDBRepository
) : BaseUseCase(), RateUseCase {

    override suspend fun getRates(): Result<Failure, RatesModel> = withIO {
        rateRepository.getRates()
    }

    override suspend fun getActualRates(base: String): Result<Failure, ActualRatesModel> = withIO {
        rateRepository.getActualRates(base)
    }

    override fun saveRates(rates: List<RateModel>) {
        secretDBRepository.putRates(rates = rates)
    }

    override suspend fun getSaveRates(): List<RateModel> = withIO {
        secretDBRepository.getRates()
    }

}