import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.*;

public class LoomFiberCounter_nCopies {
	public static void main(String[] args) throws InterruptedException {
		AtomicInteger count = new AtomicInteger();
		ExecutorService pool = Executors.newVirtualThreadExecutor();
		pool.invokeAll(Collections.nCopies(1000000, count::incrementAndGet));
		pool.shutdown();
		try {
			if (!pool.awaitTermination(60, TimeUnit.SECONDS)) {
				pool.shutdownNow();
			}
		} catch (InterruptedException ie) {
			pool.shutdownNow();
			Thread.currentThread().interrupt();
		}
		System.out.println(count.get());
	}
}
