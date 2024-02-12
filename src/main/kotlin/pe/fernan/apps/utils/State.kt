package pe.fernan.apps.utils


sealed class DataState<out R> {
    data class Success<out T>(val data: T) : DataState<T>()
    data class Error(val message: String) : DataState<Nothing>()
}


sealed class NetworkResult<T : Any> {
    class Success<T : Any>(val data: T) : NetworkResult<T>()
    class Error<T : Any>(val code: Int, val message: String?) : NetworkResult<T>()
    class Exception<T : Any>(val throwable: Throwable) : NetworkResult<T>()
}

fun <T : Any> NetworkResult<T>.toDataState(): DataState<out T> = when (this) {
    is NetworkResult.Error -> DataState.Error("$code $message")
    is NetworkResult.Exception -> DataState.Error("${throwable.message}")
    is NetworkResult.Success -> DataState.Success(data)
}




