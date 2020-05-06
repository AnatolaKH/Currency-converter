package akh.presentation.ui.features.converter

import akh.core.model.RateModel
import akh.core.model.RatesState
import akh.core.usecase.RateScreenUseCase
import akh.presentation.ui.base.BaseViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import javax.inject.Inject

class ConverterViewModel @Inject constructor(
    private val rateScreenUseCase: RateScreenUseCase
) : BaseViewModel() {

    val rateLiveData: LiveData<RatesState> = rateScreenUseCase.rateLiveData

    init {
        getRates()
    }

    fun getRates() = viewModelScope.launch {
        rateScreenUseCase.getRates()
    }

    fun exchange(exchange: String) = viewModelScope.launch {
        rateScreenUseCase.exchange(exchange)
    }

    fun setTarget(rate: RateModel) = viewModelScope.launch {
        rateScreenUseCase.setTarget(rate)
    }

    override fun onCleared() {
        super.onCleared()
        rateScreenUseCase.onCleared()
    }

}