package akh.data.request

import akh.core.base.Failure
import retrofit2.Response
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import java.util.concurrent.CancellationException

fun <T> Response<T>.isSucceed(): Boolean {
    return isSuccessful && body() != null
}

fun Int.mapToFailureState(): Failure =
    when (this) {
        401 -> Failure.HTTP401
        else -> Failure.HTTPUnknown(Throwable())
    }

fun Throwable.mapToFailureState(): Failure =
    when (this) {
        is UnknownHostException -> Failure.NetworkConnection
        is CancellationException -> Failure.Ignore
        is SocketTimeoutException -> Failure.TIMEOUT
        else -> Failure.Unknown(this)
    }