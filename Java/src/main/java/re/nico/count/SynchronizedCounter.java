package re.nico.count;

public final class SynchronizedCounter implements Counter {

    private volatile int i;

    public SynchronizedCounter() {
        i = 0;
    }

    @Override
    public synchronized void increment() {
        ++i;
    }

    @Override
    public synchronized void decrement() {
        --i;
    }

    @Override
    public synchronized int get() {
        return i;
    }
}
