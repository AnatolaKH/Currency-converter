package akh.domain.usecase.rate

import akh.core.base.Failure
import akh.core.model.RateModel
import akh.core.model.RatesModel
import akh.core.model.RatesState
import akh.core.usecase.RateScreenUseCase
import akh.core.usecase.RateUpdateUseCase
import akh.core.usecase.RateUseCase
import akh.domain.common.calculateExchange
import akh.domain.common.getRates
import akh.domain.common.withDefault
import akh.domain.usecase.BaseUseCase
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.withContext
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList
import kotlin.concurrent.timer


class RateScreenUseCaseImpl @Inject constructor(
    private val rateUseCase: RateUseCase,
    private val rateUpdateUseCase: RateUpdateUseCase
) : BaseUseCase(), RateScreenUseCase {

    private val rateMutableLiveData = MutableLiveData<RatesState>()
    override val rateLiveData: LiveData<RatesState> = rateMutableLiveData

    private fun setRates(ratesModel: RatesModel) {
        val rates = ratesModel.rates.toMutableList()
        rates.add(0, ratesModel.base)
        calculateRates(ratesModel.base.exchange, rates)
        postSuccessState(rates)
        releaseRatesAutoUpdates()
    }

    private fun calculateRates(exchange: String, rates: MutableList<RateModel>) {
        rates.forEachIndexed { index, rate -> if (index != 0) rate.calculateExchange(exchange) }
    }

    private fun releaseRatesAutoUpdates() {
        stopRatesAutoUpdates()
        startRatesAutoUpdates()
    }

    private var rateTimer: Timer? = null

    private fun startRatesAutoUpdates() {
        rateTimer = timer(initialDelay = 1000, daemon = true, period = 1000) { updateRates() }
    }

    private fun stopRatesAutoUpdates() {
        rateTimer?.apply {
            cancel()
            purge()
        }
    }

    private fun postProgressState() =
        rateMutableLiveData.postValue(RatesState.LoadingState)

    private fun postSuccessState(rates: List<RateModel>) =
        rateMutableLiveData.postValue(RatesState.SuccessState(rates))

    private fun postFailureState(failure: Failure) =
        rateMutableLiveData.postValue(RatesState.FailureState(failure))

    @Synchronized
    private fun getLastRates() = rateLiveData.getRates()

    override suspend fun getRates() {
        postProgressState()
        withDefault {
            rateUseCase.getRates().either(::postFailureState, ::setRates)
        }
    }

    private fun updateRates() {
        rateUpdateUseCase.updateRates({ getLastRates() ?: emptyList() }, ::postSuccessState)
    }

    @Synchronized
    override suspend fun setTarget(rate: RateModel) = withDefault {
        getLastRates()?.let { lastRates ->
            val rates = ArrayList<RateModel>().apply {
                lastRates.forEach { rate -> add(rate.copy(isBase = false)) }
            }
            val targetIndex = rates.indexOfFirst { mRate -> mRate.countryCode == rate.countryCode }
            if (targetIndex < 0) return@withDefault
            val temp = rates[targetIndex].copy(isBase = true)
            rates.removeAt(targetIndex)
            rates.add(0, temp)
            postSuccessState(rates)
        } ?: Unit
    }

    override suspend fun exchange(exchange: String) = withDefault {
        getLastRates()?.firstOrNull()?.exchange = exchange
        val rates = ArrayList<RateModel>().apply {
            getLastRates()?.forEach { rate -> add(rate.copy()) }
        }
        calculateRates(exchange, rates.toMutableList())
        postSuccessState(rates)
    }

    override fun onCleared() {
        stopRatesAutoUpdates()
        rateUpdateUseCase.onCleared()
    }

}