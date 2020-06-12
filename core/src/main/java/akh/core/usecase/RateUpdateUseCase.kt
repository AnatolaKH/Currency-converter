package akh.core.usecase

import akh.core.base.Failure
import akh.core.model.RateModel

interface RateUpdateUseCase : BaseUseCase {

    fun updateRates(
        getActualRates: () -> List<RateModel>,
        updateRates: (List<RateModel>) -> Unit,
        failure: (Failure) -> Unit
    )

    fun unsubscribe()

}