package pe.fernan.apps.controllers

import kotlinx.coroutines.runBlocking
import pe.fernan.apps.utils.DataState
import pe.fernan.apps.utils.toJson

internal inline fun <reified T> returnDefault(
    response: spark.Response,
    crossinline dataState: suspend () -> DataState<T>
): String {
    return runBlocking {
        return@runBlocking when (val state = dataState()) {
            is DataState.Error -> {
                response.status(400)
                state.message
            }

            is DataState.Success -> {
                state.data!!.toJson()
            }
        }
    }
}

internal fun checkParams(response: spark.Response, vararg args: Any?, onSafe: () -> String) = if (args.any { it == null }) {
    response.status(400)
    "Invalid parameters"
} else {
    onSafe()
}