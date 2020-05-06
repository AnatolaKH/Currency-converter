package akh.core.usecase

import akh.core.model.RateModel
import akh.core.model.RatesState
import androidx.lifecycle.LiveData

interface RateScreenUseCase: BaseUseCase {

    val rateLiveData: LiveData<RatesState>

    suspend fun setTarget(rate: RateModel)

    suspend fun exchange(exchange: String)

    suspend fun getRates()

}