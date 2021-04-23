package re.nico.count;

public final class SynchronizedObjectCounter implements Counter {
    private volatile int i;
    private Object lock = new Object();
    public SynchronizedObjectCounter() {
        i = 0;
    }
    @Override public void increment() {
        synchronized(lock) {
            ++i;
        }
    }
    @Override public void decrement() {
        synchronized(lock) {
            --i;
        }
    }
    @Override public int get() {
        synchronized(lock) {
            return i;
        }
    }
}
