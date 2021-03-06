package akh.domain.usecase

import akh.core.base.Failure
import akh.core.base.Result
import akh.core.usecase.RateUpdateUseCase
import akh.core.usecase.RateUseCase
import akh.domain.base.BaseUseCaseTest
import akh.domain.usecase.rate.RateScreenUseCaseImpl
import io.mockk.*
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Before
import org.junit.Test

class RateScreenUseCaseTest : BaseUseCaseTest<RateScreenUseCaseImpl>() {

    private val rateUseCase = mockk<RateUseCase>(relaxed = true)
    private val rateUpdateUseCase = mockk<RateUpdateUseCase>(relaxed = true)

    @Before
    override fun setUp() {
        super.setUp()
        useCase =
            spyk(RateScreenUseCaseImpl(rateUseCase, rateUpdateUseCase), recordPrivateCalls = true)
    }

    @Test
    fun `Get rates - response success`() = runBlocking {
        // prepare
        val fakeResponse = getFakeRatesResponse()
        val fakeRates = mutableListOf(fakeResponse.base).apply { addAll(fakeResponse.rates) }
        val response = Result.Success(fakeResponse)
        coEvery { rateUseCase.getSaveRates() } coAnswers { fakeRates }
        coEvery { rateUseCase.getRates() } coAnswers { response }
        // call
        useCase.getRates()
        coVerifyOrder {
            useCase.getRates()
            useCase["postLoadingState"]()
            useCase getProperty "useCaseScope"
            useCase["postSuccessState"](fakeRates)
            useCase getProperty "useCaseScope"
            useCase["releaseRatesAutoUpdates"]()
            useCase["stopRatesAutoUpdates"]()
            useCase["startRatesAutoUpdates"]()
        }
        confirmVerified(useCase)
        // assert

        Assert.assertNotNull(useCase.rates.first())
        Assert.assertEquals(useCase.rates.first(), fakeRates)
    }

    @Test
    fun `Get rates - response failure`() = runBlocking {
        // prepare
        val response = Result.Error(Failure.SimpleFailure(""))
        coEvery { rateUseCase.getSaveRates() } coAnswers { emptyList() }
        coEvery { rateUseCase.getRates() } coAnswers { response }
        // call
        useCase.getRates()
        coVerifyOrder {
            useCase.getRates()
            useCase["postLoadingState"]()
            useCase getProperty "useCaseScope"
            useCase["postFailureState"](response.a)
            useCase getProperty "useCaseScope"
        }
        confirmVerified(useCase)
        // assert
        Assert.assertNotNull(useCase.failure.first())
    }
}