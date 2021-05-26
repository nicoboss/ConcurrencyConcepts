import java.net.*;
import channel.*
import java.util.concurrent.Semaphore

suspend fun httpGetMultible(result: SendChannel<String>, guard: Semaphore) {
	result.send(URL("http://www.nicobosshard.ch/Hi.html").readText())
	guard.release()
}
suspend fun multibleCoroutines() {
	val tasks = 10        //10 Downloads
	val maxCoroutines = 4 //Maximal 4 gleichzeitige Corutines
	val guard = Semaphore(maxCoroutines)
	val result = Channel<String>(tasks)
	for (i in 1..tasks) {
		guard.acquireUninterruptibly()
		go { httpGetMultible(result, guard) }
	}
	for (i in 0..tasks-1) {
		var result: String = result.receive()
		println("$i: $result")
	}
}
