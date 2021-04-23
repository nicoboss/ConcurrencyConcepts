package re.nico.count;

public final class UnsafeCounter implements Counter {
    private volatile int i;
    public UnsafeCounter() {
        i = 0;
    }
    @Override public void increment() {
        ++i;
    }
    @Override public void decrement() {
        --i;
    }
    @Override public int get() {
        return i;
    }
}
