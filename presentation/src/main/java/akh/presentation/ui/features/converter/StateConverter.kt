package akh.presentation.ui.features.converter

import akh.core.base.Failure
import akh.core.model.RateModel
import akh.core.model.RatesState
import androidx.lifecycle.MutableLiveData

class StateConverter(
    private val liveData: MutableLiveData<RatesState>
) {

    fun rates(rates: List<RateModel>) =
        liveData.reduce(ConverterAction.Rates(rates))

    fun loading(loading: Boolean) =
        liveData.reduce(ConverterAction.Loading(loading))

    fun failure(failure: Failure) =
        liveData.reduce(ConverterAction.Failure(failure))

    private fun MutableLiveData<RatesState>.reduce(action: ConverterAction) {
        value = when (action) {
            is ConverterAction.Rates -> getState().onRates(action.rates)
            is ConverterAction.Loading -> getState().onLoading(action.loading)
            is ConverterAction.Failure -> getState().onFailure(action.failure)
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


    private fun getState(): RatesState =
        liveData.value ?: RatesState(showProgress = false, rates = emptyList(), failure = null)
}

sealed class ConverterAction {
    class Rates(val rates: List<RateModel>) : ConverterAction()
    class Loading(val loading: Boolean) : ConverterAction()
    class Failure(val failure: akh.core.base.Failure) : ConverterAction()
}