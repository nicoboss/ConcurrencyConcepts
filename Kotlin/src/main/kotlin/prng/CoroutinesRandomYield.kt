import java.net.*;
import channel.*

//Basieren auf C++ splitmix PRNG von Arvid Gerstmann.
fun randomYield(seed_arg: ULong) = sequence {
	var seed = seed_arg;
	while (true) {
		seed += 0x9E3779B97F4A7C15u
		var z = seed
		z = (z xor (z shr 30)) * 0xBF58476D1CE4E5B9u
		z = (z xor (z shr 27)) * 0x94D049BB133111EBu
		println("Generated!")
		yield((z xor (z shr 31)) shr 31)
	}
}

suspend fun randomYieldCaller(seed_arg: ULong, c: SendChannel<ULong>) {
	randomYield(seed_arg).forEach {
		c.send(it)
	}
}

suspend fun coroutinesRandomYield() {
	val cache = 10
	val tasks = 100
	val c = Channel<ULong>(cache)
	go { random(0u, c) }
	for (i in 0..tasks) {
		println(c.receive())
	}
	println("Done")
}
