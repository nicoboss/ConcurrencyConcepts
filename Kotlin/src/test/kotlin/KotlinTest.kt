import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.assertEquals
import channel.*

class KotlinTest {

	var expectedResponse: String = "<html><body>Hi!</body></html>"

	@Test
	fun testCoroutines() {
        println("testCoroutines:")
		var realResult: String = ""
		mainBlocking {
			realResult = coroutines()
		}
		assertEquals(expectedResponse, realResult)
		println("testCoroutines Done!")
	}

    @Test
	fun testMultibleCoroutines() {
        println("testMultibleCoroutines:")
		mainBlocking {
			multibleCoroutines()
		}
		println("testMultibleCoroutines Done!")
	}

    @Test
	fun testmultibleCoroutinesBlocking() {
        println("testMultibleCoroutines:")
		mainBlocking {
			multibleCoroutinesBlocking()
		}
		println("testMultibleCoroutines Done!")
	}

	@Test
	fun testCoroutinesRandom() {
        println("testCoroutinesRandom:")
		mainBlocking {
			coroutinesRandom()
		}
        println("testCoroutinesRandom Done!")
	}

    @Test
	fun testCoroutinesRandomYield() {
        println("testCoroutinesRandomYield:")
		mainBlocking {
			coroutinesRandomYield()
		}
        println("testCoroutinesRandomYield Done!")
	}
	
}
