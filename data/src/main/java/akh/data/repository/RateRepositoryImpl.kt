package akh.data.repository

import akh.core.base.Failure
import akh.core.base.Result
import akh.core.model.ActualRatesModel
import akh.core.model.RatesModel
import akh.core.repository.RateRepository
import akh.data.api.RateApi
import akh.data.mapper.mapToActualRatesModel
import akh.data.mapper.mapToRatesModel
import akh.data.model.RatesResponseModel
import akh.data.request.NetworkRequest
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Named

class RateRepositoryImpl @Inject constructor(
    @Named("v1") private val networkRequest: NetworkRequest,
    private val rateApi: RateApi
) : BaseRepository(), RateRepository {

    override suspend fun getActualRates(base: String): Result<Failure, ActualRatesModel> =
        networkRequest.make(
            { rateApi.getRates(base) },
            ::actualRatesResponseTransformer
        )

    private suspend fun actualRatesResponseTransformer(response: RatesResponseModel): ActualRatesModel =
        withContext(bgDispatcherDefault) {
            response.mapToActualRatesModel()
        }

    override suspend fun getRates(): Result<Failure, RatesModel> =
        networkRequest.make(
            { rateApi.getRates() },
            ::ratesResponseTransformer
        )

    private suspend fun ratesResponseTransformer(response: RatesResponseModel): RatesModel =
        withContext(bgDispatcherDefault) {
            response.mapToRatesModel()
        }

}