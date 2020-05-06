package akh.domain.usecase.rate

import akh.core.base.onNext
import akh.core.model.ActualRatesModel
import akh.core.model.RateModel
import akh.core.usecase.RateUpdateUseCase
import akh.core.usecase.RateUseCase
import akh.domain.common.getBaseCountryCode
import akh.domain.common.updateRates
import akh.domain.common.withDefault
import akh.domain.usecase.BaseUseCase
import android.os.Handler
import android.os.Looper
import android.util.Log
import kotlinx.coroutines.*
import java.io.Closeable
import java.io.IOException
import java.util.HashMap
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

class RateUpdateUseCaseImpl @Inject constructor(
    private val rateUseCase: RateUseCase
) : BaseUseCase(), RateUpdateUseCase {

    @Volatile
    private var rateJob: Job? = null

    override fun updateRates(
        getActualRates: () -> List<RateModel>,
        updateRates: (List<RateModel>) -> Unit
    ) {
        if (rateJob?.isActive != true)
            rateJob = useCaseScope.launch {
                getLastRates(getActualRates, updateRates)
            }
    }

    private suspend fun getLastRates(
        getActualRates: () -> List<RateModel>,
        updateRates: (List<RateModel>) -> Unit
    ) = withDefault {
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

    override fun unsubscribe() =
        useCaseScope.coroutineContext.cancelChildren()

    override fun onCleared() = with(useCaseScope.coroutineContext) {
        cancelChildren()
        cancel()
    }

}