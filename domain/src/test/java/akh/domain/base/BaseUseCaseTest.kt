package akh.domain.base

import akh.domain.usecase.BaseUseCase
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import io.mockk.MockKAnnotations
import org.junit.Before
import org.junit.Rule
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
abstract class BaseUseCaseTest<U: BaseUseCase> {

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()
    @get:Rule
    val coroutinesTestRule: CoroutinesTestRule = CoroutinesTestRule()

    lateinit var useCase: U

    @Before
    open fun setUp() {
        MockKAnnotations.init(this)
    }

}