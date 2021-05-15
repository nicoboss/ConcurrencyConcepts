import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ArrayBlockingQueue;

public class LoomPRNG {
	
    public static void main(String[] args) {
		BlockingQueue<Long> queue = new ArrayBlockingQueue<>(10);
		var fiber = Thread.startVirtualThread(() -> {
			var seed = 1;
            final long ulong1 = Long.parseUnsignedLong("11400714819323198485");
            final long ulong2 = Long.parseUnsignedLong("13787848793156543929");
            final long ulong3 = Long.parseUnsignedLong("10723151780598845931");
            while(true) {
                seed += ulong1;
                long z = seed;
                z = (z ^ (z >> 30)) * ulong2;
                z = (z ^ (z >> 27)) * ulong3;
                System.out.println("Generated!");
				try {
					queue.put((z ^ (z >> 31)) >> 31);
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
