package akh.domain.common

import akh.core.model.ActualRatesModel
import akh.core.model.RateModel
import java.math.BigDecimal
import java.math.RoundingMode

fun RateModel.calculateExchange(exchange: String) {
    this.exchange =
        BigDecimal(this.rate * (exchange.toDoubleOrNull() ?: 0.0))
            .setScale(2, RoundingMode.HALF_UP)
            .toString()
}

fun List<RateModel>.getBaseCountryCode(): String =
    this.firstOrNull()?.countryCode ?: "EUR"

fun List<RateModel>.updateRates(
    newRates: MutableList<RateModel>,
    actualRates: ActualRatesModel,
    exchange: String
) {
    forEachIndexed { index, rateModel ->
        if (index == 0)
            newRates.add(rateModel.copy())
        else newRates.add(
            rateModel.copy(
                rate = actualRates.actualRates[rateModel.countryCode] ?: 0.0
            ).apply { calculateExchange(exchange) }
        )
    }
}

