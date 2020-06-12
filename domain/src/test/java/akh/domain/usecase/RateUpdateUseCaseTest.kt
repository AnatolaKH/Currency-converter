package akh.domain.usecase

import akh.core.base.Failure
import akh.core.base.Result
import akh.core.model.RateModel
import akh.core.usecase.RateUseCase
import akh.domain.base.BaseUseCaseTest
import akh.domain.usecase.rate.RateUpdateUseCaseImpl
import io.mockk.*
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Before
import org.junit.Test

class RateUpdateUseCaseTest : BaseUseCaseTest<RateUpdateUseCaseImpl>() {

    private val rateUseCase = mockk<RateUseCase>(relaxed = true)

    @Before
    override fun setUp() {
        super.setUp()
        useCase = spyk(RateUpdateUseCaseImpl(rateUseCase), recordPrivateCalls = true)
    }

    @Test
    fun `Update rates`() = runBlocking {
        // prepare
        val baseRate = "EUR"
        val response = Result.Success(getFakeActualRatesResponse())
        coEvery { rateUseCase.getActualRates(baseRate) } coAnswers { response }
        // call
        useCase.updateRates(::getActualRates, ::updateRates, failure)
        coVerifyOrder {
            useCase.updateRates(::getActualRates, ::updateRates, failure)
            useCase.useCaseScope
            useCase["getLastRates"](::getActualRates, ::updateRates, failure)
            useCase["updateRates"](response.b, getActualRates(), ::updateRates)
        }
        confirmVerified(useCase)
        // assert
        Assert.assertEquals(rates[1].rate, 0.0, 0.0)
    }

    var rates = emptyList<RateModel>()

    private fun getActualRates() = getFakeActualRates()
    private fun updateRates(rates: List<RateModel>) {
        this.rates = rates
    }

    private val failure: (Failure) -> Unit = {}
}