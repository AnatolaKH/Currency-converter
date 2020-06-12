package akh.data.db.mapper

import akh.core.model.RateModel
import akh.data.db.model.RateDB

fun RateModel.toDB() =
    RateDB(
        countryCode = countryCode,
        countryName = countryName,
        rate = rate,
        countryFlag = countryFlag,
        exchange = exchange,
        isBase = isBase
    )

fun RateDB.parse() =
    RateModel(
        countryCode = countryCode,
        countryName = countryName,
        rate = rate,
        countryFlag = countryFlag,
        exchange = exchange,
        isBase = isBase
    )