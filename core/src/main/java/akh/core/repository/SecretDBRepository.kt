package akh.core.repository

import akh.core.model.RateModel

interface SecretDBRepository {

    fun putTheme(theme: Int)
    fun getTheme(): Int

    fun putRates(rates: List<RateModel>)
    fun getRates(): List<RateModel>

}