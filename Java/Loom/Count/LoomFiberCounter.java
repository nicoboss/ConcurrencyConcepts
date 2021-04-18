import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

public class LoomFiberCounter {
	public static void main(String[] args) {
		AtomicInteger count = new AtomicInteger();
		ThreadFactory factory = Thread.ofVirtual().name("pool", 0).factory();
		ExecutorService pool = Executors.newVirtualThreadExecutor();
		for (int i = 0; i < 1000000; ++i) {
			pool.submit(() -> { count.incrementAndGet(); });
		}
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
