package re.nico.count;

public final class SynchronizedMethodsCounter implements Counter {

    private volatile int i;

    public SynchronizedMethodsCounter() {
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
