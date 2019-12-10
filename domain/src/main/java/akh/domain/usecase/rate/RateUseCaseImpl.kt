package akh.domain.usecase.rate

import akh.core.base.Failure
import akh.core.base.Result
import akh.core.model.ActualRatesModel
import akh.core.model.RatesModel
import akh.core.repository.RateRepository
import akh.core.usecase.RateUseCase
import akh.domain.usecase.BaseUseCase
import kotlinx.coroutines.withContext
import javax.inject.Inject

class RateUseCaseImpl @Inject constructor(
    private val rateRepository: RateRepository
) : BaseUseCase(), RateUseCase {

    override suspend fun getRates(): Result<Failure, RatesModel> =
        withContext(bgDispatcherIO) {
            rateRepository.getRates()
        }

    override suspend fun getActualRates(base: String): Result<Failure, ActualRatesModel> =
        withContext(bgDispatcherIO) {
            rateRepository.getActualRates(base)
        }

}