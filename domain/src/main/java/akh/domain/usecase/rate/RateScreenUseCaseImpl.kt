package akh.domain.usecase.rate

import akh.core.base.Failure
import akh.core.model.RateModel
import akh.core.model.RatesModel
import akh.core.usecase.RateScreenUseCase
import akh.core.usecase.RateUpdateUseCase
import akh.core.usecase.RateUseCase
import akh.domain.common.calculateExchange
import akh.domain.common.withDefault
import akh.domain.usecase.BaseUseCase
import kotlinx.coroutines.channels.ConflatedBroadcastChannel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList
import kotlin.concurrent.timer

class RateScreenUseCaseImpl @Inject constructor(
    private val rateUseCase: RateUseCase,
    private val rateUpdateUseCase: RateUpdateUseCase
) : BaseUseCase(), RateScreenUseCase {

    private val rateChannel = ConflatedBroadcastChannel<List<RateModel>>()
    private val loadingChannel = ConflatedBroadcastChannel<Boolean>()
    private val failureChannel = ConflatedBroadcastChannel<Failure>()

    override val rates: Flow<List<RateModel>> = rateChannel.asFlow()
    override val loading: Flow<Boolean> = loadingChannel.asFlow()
    override val failure: Flow<Failure> = failureChannel.asFlow()

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
            releaseRatesAutoUpdates()
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

    override suspend fun updateRates() {
        if (getLastRates() == null)
            getRates()
        else forceUpdateCurrentRates()
    }

    override fun onCleared() {
        stopRatesAutoUpdates()
        rateUpdateUseCase.onCleared()
    }

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
        rateTimer =
            timer(initialDelay = 1000, daemon = true, period = 30000) { updateCurrentRates() }
    }

    private fun stopRatesAutoUpdates() {
        rateTimer?.apply {
            cancel()
            purge()
        }
    }

    private fun postSuccessState(rates: List<RateModel>) {
        rateUseCase.saveRates(rates = rates)
        useCaseScope.launch {
            rateChannel.send(rates)
        }
    }

    private fun postLoadingState() {
        useCaseScope.launch {
            loadingChannel.send(true)
        }
    }

    private fun postFailureState(failure: Failure) {
        useCaseScope.launch {
            failureChannel.send(failure)
        }
    }

    @Synchronized
    private fun getLastRates(): List<RateModel>? =
        rateChannel.valueOrNull

    override suspend fun getRates() {
        postLoadingState()
        withDefault {
            val saveRates = rateUseCase.getSaveRates()
            if (saveRates.isNotEmpty()) {
                postSuccessState(saveRates)
                releaseRatesAutoUpdates()
            } else
                rateUseCase.getRates().either(::postFailureState, ::setRates)
        }
    }

    private suspend fun forceUpdateCurrentRates() = withDefault {
        rateUpdateUseCase.updateRates(
            { getLastRates() ?: emptyList() },
            ::postSuccessState,
            ::postFailureState
        )
    }

    private fun updateCurrentRates() =
        rateUpdateUseCase.updateRates({ getLastRates() ?: emptyList() }, ::postSuccessState, {})
}