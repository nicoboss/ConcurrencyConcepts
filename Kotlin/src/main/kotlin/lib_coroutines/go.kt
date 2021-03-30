//From https://raw.githubusercontent.com/Kotlin/coroutines-examples/master/examples/channel/go.kt

package channel

import context.*
import run.*

fun mainBlocking(block: suspend () -> Unit) = runBlocking(CommonPool, block)

fun go(block: suspend () -> Unit) = CommonPool.runParallel(block)
