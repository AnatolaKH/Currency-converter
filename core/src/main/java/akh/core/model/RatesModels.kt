package akh.core.model

import androidx.annotation.DrawableRes

data class RateModel(
    val countryCode: String,
    val countryName: String,
    val rate: Double,
    @DrawableRes val countryFlag: Int,
    var exchange: String,
    var isBase: Boolean = false
)

class RatesModel(
    val base: RateModel,
    val date: String,
    val rates: List<RateModel>
)

class ActualRatesModel(
    val base: String,
    val date: String,
    val actualRates: Map<String, Double>
)