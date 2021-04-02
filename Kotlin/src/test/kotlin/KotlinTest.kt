import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.assertEquals
import channel.*

class KotlinTest {

	var expectedResponse: String = "<html><body>Hi!</body></html>"

	@Test
	fun testGoroutines() {
        println("testGoroutines:")
		var realResult: String = ""
		mainBlocking {
			realResult = goroutines()
		}
		assertEquals(expectedResponse, realResult)
		println("testGoroutines Done!")
	}

    @Test
	fun testMultibleGoroutines() {
        println("testMultibleGoroutines:")
		mainBlocking {
			multibleGoroutines()
		}
		println("testMultibleGoroutines Done!")
	}

    @Test
	fun testmultibleGoroutinesBlocking() {
        println("testMultibleGoroutines:")
		mainBlocking {
			multibleGoroutinesBlocking()
		}
		println("testMultibleGoroutines Done!")
	}

	@Test
	fun testGoroutinesRandom() {
        println("testGoroutinesRandom:")
		mainBlocking {
			goroutinesRandom()
		}
        println("testGoroutinesRandom Done!")
	}

    @Test
	fun testGoroutinesRandomYield() {
        println("testGoroutinesRandomYield:")
		mainBlocking {
			goroutinesRandomYield()
		}
        println("testGoroutinesRandomYield Done!")
	}
	
}
