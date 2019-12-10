package akh.data.request

import akh.core.base.Failure
import akh.core.base.Result
import retrofit2.Response

interface NetworkRequest {

    suspend fun <T, R> make(
        request: suspend () -> Response<T?>,
        transform: suspend (T) -> R
    ): Result<Failure, R>

}