package akh.data.api

import akh.data.api.ApiUrl.RATES
import akh.data.model.RatesResponseModel
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface RateApi {

    @GET(RATES)
    suspend fun getRates(@Query("base") base: String? = null): Response<RatesResponseModel?>

}