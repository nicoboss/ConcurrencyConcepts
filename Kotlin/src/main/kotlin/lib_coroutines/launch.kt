//From: https://raw.githubusercontent.com/Kotlin/coroutines-examples/master/examples/run/launch.kt

package run

import kotlin.coroutines.*

fun launch(context: CoroutineContext = EmptyCoroutineContext, block: suspend () -> Unit) =
    block.startCoroutine(Continuation(context) { result ->
        result.onFailure { exception ->
            val currentThread = Thread.currentThread()
            currentThread.uncaughtExceptionHandler.uncaughtException(currentThread, exception)
        }
    })
