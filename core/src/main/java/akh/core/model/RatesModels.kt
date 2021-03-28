package akh.core.model

data class RateModel(
    val countryCode: String,
    val countryName: String,
    val rate: Double,
    val countryFlag: Int,
    var exchange: String,
    var isBase: Boolean = false
)

class RatesModel(
    val base: RateModel,
    val rates: List<RateModel>
)

class ActualRatesModel(
    val base: String,
    val actualRates: Map<String, Double>
)