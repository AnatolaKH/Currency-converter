package akh.data.request

import akh.core.base.Failure
import akh.core.base.Result
import retrofit2.Response
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NetworkRequestV1Impl @Inject constructor() : NetworkRequest {

    override suspend fun <T, R> make(
        request: suspend () -> Response<T?>,
        transform: suspend (T) -> R
    ): Result<Failure, R> =
        try {
            val response = request()
            if (response.isSucceed())
                Result.Success(transform(response.body()!!))
            else
                Result.Error(response.code().mapToFailureState()) //TODO map error response
        } catch (t: Throwable) {
            Timber.e(t)
            Result.Error(t.mapToFailureState())
        }

}