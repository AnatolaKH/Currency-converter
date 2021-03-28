package akh.presentation.ui.features.converter

import akh.core.base.Failure
import akh.core.model.RateModel
import akh.core.model.RatesState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class State {

    private val stateMutableFlow: MutableStateFlow<RatesState> = MutableStateFlow(initialState())
    val stateFlow: StateFlow<RatesState> = stateMutableFlow.asStateFlow()

    suspend fun reduce(action: Action) {
        stateMutableFlow.value = when (action) {
            is Action.Rates -> stateFlow.value.onRates(action.rates)
            is Action.Loading -> stateFlow.value.onLoading(action.loading)
            is Action.Failure -> stateFlow.value.onFailure(action.failure)
        }
    }

    private fun RatesState.onRates(rates: List<RateModel>) =
        copy(
            showProgress = false,
            rates = rates,
            failure = null
        )

    private fun RatesState.onLoading(loading: Boolean) =
        copy(
            showProgress = loading,
            failure = null
        )

    private fun RatesState.onFailure(failure: Failure) =
        copy(
            showProgress = false,
            failure = failure
        )

    private fun initialState() = RatesState(
        showProgress = false,
        rates = emptyList(),
        failure = null
    )
}

sealed class Action {
    class Rates(val rates: List<RateModel>) : Action()
    class Loading(val loading: Boolean) : Action()
    class Failure(val failure: akh.core.base.Failure) : Action()
}