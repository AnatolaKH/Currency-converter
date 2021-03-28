package akh.core.usecase

import akh.core.base.Failure
import akh.core.model.RateModel
import kotlinx.coroutines.flow.Flow

interface RateScreenUseCase : BaseUseCase {

    val rates: Flow<List<RateModel>>
    val loading: Flow<Boolean>
    val failure: Flow<Failure>

    suspend fun setTarget(rate: RateModel)

    suspend fun exchange(exchange: String)

    suspend fun getRates()

    suspend fun updateRates()

}