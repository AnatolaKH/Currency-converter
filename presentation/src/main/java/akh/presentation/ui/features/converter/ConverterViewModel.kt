package akh.presentation.ui.features.converter

import akh.core.model.RateModel
import akh.core.model.RatesState
import akh.core.usecase.RateScreenUseCase
import akh.presentation.common.launch
import akh.presentation.ui.base.BaseViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import javax.inject.Inject

class ConverterViewModel @Inject constructor(
    private val rateScreenUseCase: RateScreenUseCase
) : BaseViewModel() {

    private val screenStateLiveData = MediatorLiveData<RatesState>().apply {
        addSource(rateScreenUseCase.ratesLiveData, this::ratesState)
        addSource(rateScreenUseCase.loadingLiveData, this::loadingState)
        addSource(rateScreenUseCase.failureLiveData, this::failureState)
    }
    val screenState: LiveData<RatesState> = screenStateLiveData

    init {
        getRates()
    }

    fun updateRates() = launch {
        rateScreenUseCase.updateRates()
    }

    fun exchange(exchange: String) = launch {
        rateScreenUseCase.exchange(exchange)
    }

    fun setTarget(rate: RateModel) = launch {
        rateScreenUseCase.setTarget(rate)
    }

    override fun onCleared() {
        super.onCleared()
        rateScreenUseCase.onCleared()
    }

    private fun getRates() = launch {
        rateScreenUseCase.getRates()
    }
}