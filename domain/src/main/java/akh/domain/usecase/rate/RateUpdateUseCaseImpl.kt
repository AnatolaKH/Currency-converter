package akh.domain.usecase.rate

import akh.core.base.Failure
import akh.core.model.ActualRatesModel
import akh.core.model.RateModel
import akh.core.usecase.RateUpdateUseCase
import akh.core.usecase.RateUseCase
import akh.domain.common.getBaseCountryCode
import akh.domain.common.updateRates
import akh.domain.common.withDefault
import akh.domain.usecase.BaseUseCase
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.launch
import javax.inject.Inject

class RateUpdateUseCaseImpl @Inject constructor(
    private val rateUseCase: RateUseCase
) : BaseUseCase(), RateUpdateUseCase {

    @Volatile
    private var rateJob: Job? = null

    override fun updateRates(
        getActualRates: () -> List<RateModel>,
        updateRates: (List<RateModel>) -> Unit,
        failure: (Failure) -> Unit
    ) {
        if (rateJob?.isActive != true)
            rateJob = useCaseScope.launch {
                getLastRates(getActualRates, updateRates, failure)
            }
    }

    private suspend fun getLastRates(
        getActualRates: () -> List<RateModel>,
        updateRates: (List<RateModel>) -> Unit,
        failure: (Failure) -> Unit
    ) = withDefault {
        rateUseCase.getActualRates(getActualRates().getBaseCountryCode())
            .either(failure, { updateRates(it, getActualRates(), updateRates) } )
    }

    @Synchronized
    private fun updateRates(
        actualRates: ActualRatesModel,
        rates: List<RateModel>,
        updateRates: (List<RateModel>) -> Unit
    ) {
        if (rates.firstOrNull()?.countryCode != actualRates.base) return
        val items = ArrayList<RateModel>()
        rates.updateRates(items, actualRates, rates.firstOrNull()?.exchange ?: "")
        updateRates(items)
    }

    override fun unsubscribe() =
        useCaseScope.coroutineContext.cancelChildren()

    override fun onCleared() = with(useCaseScope.coroutineContext) {
        cancelChildren()
        cancel()
    }

}