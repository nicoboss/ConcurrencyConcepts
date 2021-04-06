import java.net.*;
import channel.*

suspend fun httpGetSingle(result: SendChannel<String>) {
	result.send(URL("http://www.nicobosshard.ch/Hi.html").readText())
}

suspend fun coroutines(): String {
	val result = Channel<String>()
	go { httpGetSingle(result) }
	return(result.receive())
}
