package akh.core.usecase

import akh.core.base.Failure
import akh.core.model.RateModel
import androidx.lifecycle.LiveData

interface RateScreenUseCase : BaseUseCase {

    val ratesLiveData: LiveData<List<RateModel>>
    val loadingLiveData: LiveData<Boolean>
    val failureLiveData: LiveData<Failure>

    suspend fun setTarget(rate: RateModel)

    suspend fun exchange(exchange: String)

    suspend fun getRates()

    suspend fun updateRates()

}