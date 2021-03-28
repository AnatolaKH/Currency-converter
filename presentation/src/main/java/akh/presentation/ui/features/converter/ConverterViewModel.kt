package akh.presentation.ui.features.converter

import akh.core.model.RateModel
import akh.core.model.RatesState
import akh.core.usecase.RateScreenUseCase
import akh.presentation.common.launch
import akh.presentation.ui.base.BaseViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.merge
import javax.inject.Inject

class ConverterViewModel @Inject constructor(
    private val rateScreenUseCase: RateScreenUseCase
) : BaseViewModel() {

    private val state = State()
    val stateFlow: Flow<RatesState> = state.stateFlow

    init {
        initState()
        getRates()
    }

    fun onUpdateRates() = launch {
        rateScreenUseCase.updateRates()
    }

    fun onExchange(exchange: String) = launch {
        rateScreenUseCase.exchange(exchange)
    }

    fun onSetTarget(rate: RateModel) = launch {
        rateScreenUseCase.setTarget(rate)
    }

    override fun onCleared() {
        super.onCleared()
        rateScreenUseCase.onCleared()
    }

    private fun getRates() = launch {
        rateScreenUseCase.getRates()
    }

    private fun initState() = launch {
        merge(
            ratesActions(),
            loadingActions(),
            failureActions()
        ).collect(state::reduce)
    }

    private fun ratesActions(): Flow<Action> =
        rateScreenUseCase.rates.map { rates -> Action.Rates(rates) }

    private fun loadingActions(): Flow<Action> =
        rateScreenUseCase.loading.map { loading -> Action.Loading(loading) }

    private fun failureActions(): Flow<Action> =
        rateScreenUseCase.failure.map { failure -> Action.Failure(failure) }
}