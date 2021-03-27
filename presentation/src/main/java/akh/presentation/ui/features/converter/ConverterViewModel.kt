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
        val stateConverter = StateConverter(this)
        addSource(rateScreenUseCase.ratesLiveData, stateConverter::rates)
        addSource(rateScreenUseCase.loadingLiveData, stateConverter::loading)
        addSource(rateScreenUseCase.failureLiveData, stateConverter::failure)
    }
    val screenState: LiveData<RatesState> = screenStateLiveData

    init {
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
}