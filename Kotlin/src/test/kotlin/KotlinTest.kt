import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.assertEquals
import channel.*

class KotlinTest {

	var expectedResponse: String = "<html><body>Hi!</body></html>"

	@Test
	fun testGoroutines() {
		var realResult: String = ""
		mainBlocking {
			realResult = goroutines()
		}
		assertEquals(expectedResponse, realResult)
		println("testGoroutines Done!")
	}

    @Test
	fun testMultibleGoroutines() {
		mainBlocking {
			multibleGoroutines()
		}
		println("testMultibleGoroutines Done!")
	}

	@Test
	fun testGoroutinesRandom() {
		mainBlocking {
			goroutinesRandom()
		}
		println("testGoroutinesRandom Done!")
	}
	
}
