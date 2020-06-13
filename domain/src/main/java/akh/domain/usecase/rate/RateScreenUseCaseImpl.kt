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
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList
import kotlin.concurrent.timer

class RateScreenUseCaseImpl @Inject constructor(
    private val rateUseCase: RateUseCase,
    private val rateUpdateUseCase: RateUpdateUseCase
) : BaseUseCase(), RateScreenUseCase {

    private val rateMutableLiveData: MutableLiveData<List<RateModel>> = MutableLiveData()
    private val loadingMutableLiveData: MutableLiveData<Boolean> = MutableLiveData()
    private val failureMutableLiveData: MutableLiveData<Failure> = MutableLiveData()

    override val ratesLiveData: LiveData<List<RateModel>> = rateMutableLiveData
    override val loadingLiveData: LiveData<Boolean> = loadingMutableLiveData
    override val failureLiveData: LiveData<Failure> = failureMutableLiveData

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
        rateMutableLiveData.postValue(rates)
    }

    private fun postLoadingState() = loadingMutableLiveData.postValue(true)

    private fun postFailureState(failure: Failure) = failureMutableLiveData.postValue(failure)

    @Synchronized
    private fun getLastRates() = ratesLiveData.value

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

    override suspend fun updateRates() {
        if (getLastRates() == null)
            getRates()
        else forceUpdateCurrentRates()
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

    override fun onCleared() {
        stopRatesAutoUpdates()
        rateUpdateUseCase.onCleared()
    }
}