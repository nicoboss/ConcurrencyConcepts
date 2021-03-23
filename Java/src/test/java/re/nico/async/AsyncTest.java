package re.nico.async;

import static org.junit.jupiter.api.Assertions.assertEquals;
import java.util.concurrent.ExecutionException;
import org.junit.jupiter.api.Test;

final class AsyncTest {

    String expectedResponse = "<html><body>Hi!</body></html>";

    @Test
    void testBasicsFuture() throws InterruptedException, ExecutionException {
        assertEquals(expectedResponse, BasicsFuture.run());
    }

    @Test
    void testAsyncHandlers() throws InterruptedException, ExecutionException {
        assertEquals(expectedResponse, AsyncHandlers.run());
    }

    @Test
    void testCompletableFuture() {
        assertEquals(expectedResponse, CompletableFuture.run());
    }

    @Test
    void testListenableFutureDemo() throws InterruptedException, ExecutionException {
        assertEquals(expectedResponse, ListenableFutureDemo.run());
    }

    @Test
    void testReactiveStreams() throws InterruptedException, ExecutionException, Throwable {
        assertEquals(expectedResponse, ReactiveStreams.run());
    }

}
