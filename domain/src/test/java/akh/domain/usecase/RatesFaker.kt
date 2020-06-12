package akh.domain.usecase

import akh.core.model.ActualRatesModel
import akh.core.model.RateModel
import akh.core.model.RatesModel

fun getFakeRatesResponse() = RatesModel(
    base = RateModel(
        "EUR",
        "EURO",
        1.0,
        0,
        "100",
        true
    ),
    rates = listOf(
        RateModel(
            "USD",
            "US Dollar",
            5.0,
            0,
            "",
            true
        )
    )
)

fun getFakeActualRatesResponse() = ActualRatesModel(
    "EUR",
    mapOf()
)

fun getFakeActualRates() = listOf(
    RateModel(
        "EUR",
        "EURO",
        1.0,
        0,
        "100",
        true
    ),
    RateModel(
        "USD",
        "US Dollar",
        2.0,
        0,
        "100",
        true
    )
)
