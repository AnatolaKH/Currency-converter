package akh.presentation.ui.features.converter

import akh.core.base.Failure
import akh.core.model.RateModel
import akh.core.model.RatesState
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

fun MutableLiveData<RatesState>.ratesState(rates: List<RateModel>): Unit =
    postValue(
        getState().copy(
            showProgress = false,
            rates = rates,
            failure = null
        )
    )

fun MutableLiveData<RatesState>.loadingState(isLoading: Boolean): Unit =
    postValue(
        getState().copy(
            showProgress = isLoading,
            failure = null
        )
    )

fun MutableLiveData<RatesState>.failureState(failure: Failure): Unit =
    postValue(
        getState().copy(
            showProgress = false,
            failure = failure
        )
    )

private fun LiveData<RatesState>.getState(): RatesState =
    this.value ?: RatesState(showProgress = false, rates = emptyList(), failure = null)
