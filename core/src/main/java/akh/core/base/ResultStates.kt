package akh.core.base

sealed class Result<out L, out R> {

    data class Error<out L>(val a: L) : Result<L, Nothing>()

    data class Success<out R>(val b: R) : Result<Nothing, R>()

    val isSuccess get() = this is Success<R>
    val isError get() = this is Error<L>

    fun <L> left(a: L) = Error(a)
    fun <R> right(b: R) = Success(b)

    fun either(functionLeft: (L) -> Any, functionRight: (R) -> Any): Any =
        when (this) {
            is Error -> functionLeft(a)
            is Success -> functionRight(b)
        }

    suspend fun sEither(functionLeft: suspend (L) -> Any, functionRight: suspend (R) -> Any): Any =
        when (this) {
            is Error -> functionLeft(a)
            is Success -> functionRight(b)
        }
}

fun <A, B, C> ((A) -> B).compose(f: (B) -> C): (A) -> C = {
    f(this(it))
}

fun <T, L, R> Result<L, R>.flatMap(fn: (R) -> Result<L, T>): Result<L, T> {
    return when (this) {
        is Result.Error -> Result.Error(a)
        is Result.Success -> fn(b)
    }
}

suspend fun <T, L, R> Result<L, R>.suspendFlatMap(fn: suspend (R) -> Result<L, T>): Result<L, T> {
    return when (this) {
        is Result.Error -> Result.Error(a)
        is Result.Success -> fn(b)
    }
}

fun <T, L, R> Result<L, R>.map(fn: (R) -> (T)): Result<L, T> {
    return this.flatMap(fn.compose(::right))
}

fun <L, R> Result<L, R>.onNext(fn: (R) -> Unit): Result<L, R> {
    this.flatMap(fn.compose(::right))
    return this
}

sealed class Failure {

    class SimpleFailure(val error: String) : Failure()

    object NetworkConnection : Failure()

    object HTTP401 : Failure()

    object TIMEOUT : Failure()

    class HTTPUnknown(val error: Throwable) : Failure()

    class Unknown(val error: Throwable) : Failure()

    object Ignore : Failure()

}

