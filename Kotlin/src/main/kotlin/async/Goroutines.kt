import java.net.*;
import channel.*

suspend fun getGoogle(c: SendChannel<String>) {
    c.send(URL("http://www.nicobosshard.ch/Hi.html").readText())
}

suspend fun goroutines(): String {
    val c = Channel<String>()
    go { getGoogle(c) }
    return(c.receive())
}
