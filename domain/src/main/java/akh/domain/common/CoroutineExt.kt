package akh.domain.common

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

suspend fun <T> withIO(block: suspend CoroutineScope.() -> T): T =
    withContext(Dispatchers.IO, block = block)

suspend fun <T> withDefault(block: suspend CoroutineScope.() -> T): T =
    withContext(Dispatchers.Default, block = block)