package akh.domain.usecase

import akh.core.base.Failure
import akh.core.base.Result
import akh.core.repository.RateRepository
import akh.domain.base.BaseUseCaseTest
import akh.domain.usecase.rate.RateUseCaseImpl
import io.mockk.*
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Before
import org.junit.Test

class RateUseCaseTest : BaseUseCaseTest<RateUseCaseImpl>() {

    private val rateRepository = mockk<RateRepository>(relaxed = true)

    @Before
    override fun setUp() {
        super.setUp()
        useCase = spyk(RateUseCaseImpl(rateRepository), recordPrivateCalls = true)
    }

    @Test
    fun `Get rates - response success`() = runBlocking {
        // prepare
        val response = Result.Success(getFakeRatesResponse())
        coEvery { rateRepository.getRates() } coAnswers { response }
        // call
        val result = useCase.getRates()
        coVerifyOrder {
            useCase.getRates()
            useCase.bgDispatcherIO
        }
        confirmVerified(useCase)
        // assert
        Assert.assertNotNull(result)
        Assert.assertEquals(result, response)
    }

    @Test
    fun `Get rates - response failure`() = runBlocking {
        // prepare
        val response = Result.Error(Failure.SimpleFailure(""))
        coEvery { rateRepository.getRates() } coAnswers { response }
        // call
        val result = useCase.getRates()
        coVerifyOrder {
            useCase.getRates()
            useCase.bgDispatcherIO
        }
        confirmVerified(useCase)
        // assert
        Assert.assertNotNull(result)
        Assert.assertEquals(result, response)
    }


    @Test
    fun `Get actual rates - response success`() = runBlocking {
        // prepare
        val response = Result.Success(getFakeActualRatesResponse())
        coEvery { rateRepository.getActualRates("EUR") } coAnswers { response }
        // call
        val result = useCase.getActualRates("EUR")
        coVerifyOrder {
            useCase.getActualRates("EUR")
            useCase.bgDispatcherIO
        }
        confirmVerified(useCase)
        // assert
        Assert.assertNotNull(result)
        Assert.assertEquals(result, response)
    }

    @Test
    fun `Get actual rates - response failure`() = runBlocking {
        // prepare
        val response = Result.Error(Failure.SimpleFailure(""))
        coEvery { rateRepository.getActualRates("EUR") } coAnswers { response }
        // call
        val result =  useCase.getActualRates("EUR")
        coVerifyOrder {
            useCase.getActualRates("EUR")
            useCase.bgDispatcherIO
        }
        confirmVerified(useCase)
        // assert
        Assert.assertNotNull(result)
        Assert.assertEquals(result, response)
    }

}