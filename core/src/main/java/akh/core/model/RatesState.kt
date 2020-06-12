package akh.core.model

import akh.core.base.Failure

data class RatesState(
    val showProgress: Boolean,
    val rates: List<RateModel>,
    val failure: Failure?
)