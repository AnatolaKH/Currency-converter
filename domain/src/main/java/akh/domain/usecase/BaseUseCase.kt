package akh.domain.usecase

import akh.core.usecase.BaseUseCase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob


abstract class BaseUseCase: BaseUseCase {

    open val useCaseScopeDispatcher: CoroutineDispatcher = Dispatchers.IO

    val useCaseScope: CoroutineScope by lazy {
        CoroutineScope(SupervisorJob() + useCaseScopeDispatcher)
    }

    override fun onCleared() = Unit

}