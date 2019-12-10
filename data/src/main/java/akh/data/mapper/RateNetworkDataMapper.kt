package akh.data.mapper

import akh.core.model.ActualRatesModel
import akh.core.model.RateModel
import akh.core.model.RatesModel
import akh.data.R
import akh.data.model.RatesResponseModel
import androidx.annotation.DrawableRes

fun RatesResponseModel.mapToRatesModel(): RatesModel {
    val rates = ArrayList<RateModel>()

    this.rates?.keys?.forEach { country ->
        if (country != null)
            rates.add(
                RateModel(
                    countryCode = country,
                    countryName = country.mapToFullCountryName(),
                    rate = this.rates[country] ?: 0.0,
                    countryFlag = country.mapToCountryFlag(),
                    exchange = ""
                )
            )
    }

    return RatesModel(
        base = RateModel(
            base ?: "",
            base?.mapToFullCountryName() ?: "",
            0.0,
            base.mapToCountryFlag(),
            "100",
            true
        ),
        date = date ?: "",
        rates = rates
    )
}

fun String.mapToFullCountryName(): String = RateCountryMapper.countryNameMap[this] ?: "Undefined"

@DrawableRes
fun String?.mapToCountryFlag(): Int = RateCountryMapper.countryFlagMap[this] ?: R.drawable.ic_eur

fun RatesResponseModel.mapToActualRatesModel(): ActualRatesModel {
    val actualRates = HashMap<String, Double>()

    this.rates?.keys?.forEach { key ->
        if (key != null && this.rates[key] != null)
            actualRates[key] = this.rates[key] ?: 0.0
    }
    return ActualRatesModel(
        base = this.base ?: "",
        date = date ?: "",
        actualRates = actualRates
    )
}

