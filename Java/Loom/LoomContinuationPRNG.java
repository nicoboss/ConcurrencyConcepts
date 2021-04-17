import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ArrayBlockingQueue;

public class LoomContinuationPRNG {
	
    public static void main(String[] args) {
		BlockingQueue<Long> queue = new ArrayBlockingQueue<>(10);
		var a = Thread.startVirtualThread(() -> {
			AtomicLong result = new AtomicLong();
			var scope = new ContinuationScope("LoomPRNG");
			var continuation = new Continuation(scope, () -> {
				var seed = 1;
				long ulong1 = Long.parseUnsignedLong("11400714819323198485");
				long ulong2 = Long.parseUnsignedLong("13787848793156543929");
				long ulong3 = Long.parseUnsignedLong("10723151780598845931");
				while(true) {
					seed += ulong1;
					long z = seed;
					z = (z ^ (z >> 30)) * ulong2;
					z = (z ^ (z >> 27)) * ulong3;
					result.set((z ^ (z >> 31)) >> 31);
					System.out.println("Generated!");
					Continuation.yield(scope);
				}
			});
			
			while (true) {
				System.out.println("Request next!");
				continuation.run();
				try {
					queue.put(result.get());
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
        });

        for(int i = 0; i < 100; ++i) {
			try {
				System.out.println(queue.take());
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
        }
        System.out.println("Done!");
    }
}
