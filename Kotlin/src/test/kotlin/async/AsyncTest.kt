import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.assertEquals
import channel.*

class AsyncTest {

    var expectedResponse: String = "<html><body>Hi!</body></html>"

    @Test
    fun testGoroutines() {
        var realResult: String = ""
        mainBlocking {
            realResult = goroutines()
        }
        assertEquals(expectedResponse, realResult)
        println("Done!")
    }
}
