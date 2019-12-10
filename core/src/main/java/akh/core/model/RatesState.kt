package akh.core.model

import akh.core.base.Failure


sealed class RatesState{
    object LoadingState: RatesState()
    class FailureState(val failure: Failure): RatesState()
    class SuccessState(val rates: List<RateModel>): RatesState()
}



