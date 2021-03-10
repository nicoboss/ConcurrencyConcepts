package re.nico.count;

public final class SynchronizedThisCounter implements Counter {

    private volatile int i;

    public SynchronizedThisCounter() {
        i = 0;
    }

    @Override
    public void increment() {
        synchronized(this) {
            ++i;
        }
    }

    @Override
    public void decrement() {
        synchronized(this) {
            --i;
        }
        
    }

    @Override
    public int get() {
        synchronized(this) {
            return i;
        }
    }
}
