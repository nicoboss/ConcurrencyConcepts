import channel.*
import java.net.*;

suspend fun getGoogle(c: SendChannel<String>) {
    c.send(URL("http://www.nicobosshard.ch/Hi.html").readText())
}

fun main(args: Array<String>) = mainBlocking {
//suspend fun runMe() {
    try{
        val c = Channel<String>()
        go { getGoogle(c) }
        println("Task")
        println(c.receive())
    } catch (e: IllegalThreadStateException) {}
}
