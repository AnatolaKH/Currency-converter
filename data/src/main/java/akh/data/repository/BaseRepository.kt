package akh.data.repository

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

abstract class BaseRepository {

    var bgDispatcherIO: CoroutineDispatcher = Dispatchers.IO
    var bgDispatcherDefault: CoroutineDispatcher = Dispatchers.Default

}