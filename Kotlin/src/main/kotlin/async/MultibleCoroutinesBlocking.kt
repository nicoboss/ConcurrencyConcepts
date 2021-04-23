import java.net.*;
import channel.*
import java.util.concurrent.Semaphore
import kotlin.coroutines.*

suspend fun httpGetMultibleBlocking(result: SendChannel<String>) {
	result.send(URL("http://www.nicobosshard.ch/Hi.html").readText())
}
suspend fun multibleCoroutinesBlocking() {
	val tasks = 10        //10 Downloads
	val maxGoroutines = 4 //Maximal 4 gleichzeitige Gorutines
	val guard = Semaphore(maxGoroutines)
	val result = Channel<String>(tasks)
	mainBlocking {
		for (i in 1..tasks) {
			guard.acquireUninterruptibly()
			go { httpGetMultible(result) }
			guard.release()
		}
	}
	for (i in 0..tasks-1) {
		var result: String = result.receive()
		println("$i: $result")
	}
}
