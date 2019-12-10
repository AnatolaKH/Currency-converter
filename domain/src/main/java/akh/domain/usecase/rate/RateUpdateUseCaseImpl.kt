package akh.domain.usecase.rate

import akh.core.base.onNext
import akh.core.model.ActualRatesModel
import akh.core.model.RateModel
import akh.core.usecase.RateUpdateUseCase
import akh.core.usecase.RateUseCase
import akh.domain.common.getBaseCountryCode
import akh.domain.common.updateRates
import akh.domain.usecase.BaseUseCase
import kotlinx.coroutines.*
import javax.inject.Inject

class RateUpdateUseCaseImpl @Inject constructor(
    private val rateUseCase: RateUseCase
) : BaseUseCase(), RateUpdateUseCase {

    private var rateJob: Job = Job()

    override fun updateRates(
        getActualRates: () -> List<RateModel>,
        updateRates: (List<RateModel>) -> Unit
    ) {
        if (foregroundDispatcher[Job]?.isActive == true) return

        rateJob = Job()

        CoroutineScope(foregroundDispatcher + rateJob).launch {
            getLastRates(getActualRates, updateRates)
        }
    }

    private suspend fun getLastRates(
        getActualRates: () -> List<RateModel>,
        updateRates: (List<RateModel>) -> Unit
    ) = withContext(bgDispatcherDefault) {
        rateUseCase.getActualRates(getActualRates().getBaseCountryCode())
            .onNext { updateRates(it, getActualRates(), updateRates) }
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

    override fun unsubscribe() {
        rateJob.apply {
            cancelChildren()
            cancel()
        }
    }

}