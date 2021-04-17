import java.util.concurrent.atomic.AtomicLong;

public class LoomSingleThreadedContinuationPRNG {
	
    public static void main(String[] args) {
		AtomicLong result = new AtomicLong();
        var scope = new ContinuationScope("LoomPRNG");
        var continuation = new Continuation(scope, () -> {
            var seed = 1;
            long ulong1 = Long.parseUnsignedLong("11400714819323198485");
            long ulong2 = Long.parseUnsignedLong("13787848793156543929");
            long ulong3 = Long.parseUnsignedLong("10723151780598845931");
            for (int i = 0; i < 100; ++i) {
                seed += ulong1;
                long z = seed;
                z = (z ^ (z >> 30)) * ulong2;
                z = (z ^ (z >> 27)) * ulong3;
				result.set((z ^ (z >> 31)) >> 31);
				System.out.println("Generated!");
				Continuation.yield(scope);
            }
        });

        while (!continuation.isDone()) {
            System.out.println("Request next!");
			continuation.run();
			System.out.println(result.get());
        }
        System.out.println("Done!");
    }
}
