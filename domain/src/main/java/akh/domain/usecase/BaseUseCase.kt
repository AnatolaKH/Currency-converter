package akh.domain.usecase

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers


abstract class BaseUseCase {

    var bgDispatcherIO: CoroutineDispatcher = Dispatchers.IO
    var bgDispatcherDefault: CoroutineDispatcher = Dispatchers.Default
    var foregroundDispatcher: CoroutineDispatcher = Dispatchers.Main

}