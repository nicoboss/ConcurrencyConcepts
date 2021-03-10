package re.nico.count;

public class CountTask implements Runnable {

    private final Counter counter;
    private final int counts;

    public CountTask(Counter counter, int counts) {
        this.counter = counter;
        this.counts = counts;
    }
    
    @Override
    public void run() {
        for (int i = 0; i < counts; ++i) {
            counter.increment();
        }
        for (int i = 0; i < counts; ++i) {
            counter.decrement();
        }
    }
}
