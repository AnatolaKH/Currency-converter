package akh.core.usecase

import akh.core.base.Failure
import akh.core.base.Result
import akh.core.model.ActualRatesModel
import akh.core.model.RatesModel

interface RateUseCase: BaseUseCase {

    suspend fun getRates(): Result<Failure, RatesModel>

    suspend fun getActualRates(base: String): Result<Failure, ActualRatesModel>

}