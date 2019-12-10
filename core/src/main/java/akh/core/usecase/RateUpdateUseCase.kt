package akh.core.usecase

import akh.core.model.RateModel

interface RateUpdateUseCase {

    fun updateRates(getActualRates: () -> List<RateModel>, updateRates:(List<RateModel>) -> Unit)

    fun unsubscribe()

}