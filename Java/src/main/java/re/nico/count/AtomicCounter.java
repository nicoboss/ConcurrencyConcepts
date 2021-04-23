package re.nico.count;

import java.util.concurrent.atomic.AtomicInteger;

public final class AtomicCounter implements Counter {
    private final AtomicInteger i;
    public AtomicCounter() {
        i = new AtomicInteger(0);
    }
    @Override public void increment() {
        i.incrementAndGet();
    }
    @Override public void decrement() {
        i.decrementAndGet();
    }
    @Override public int get() {
        return i.get();
    }
}
